
import helperClasses.AuxiliaryBoxesUI;
import helperClasses.Colors;
import helperClasses.LoginButton;
import helperClasses.MessageEncoder;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static helperClasses.ConstantsCodes.*;


public class ClientClass extends JFrame implements ActionListener {

    private JScrollPane listScrollConnections;
    private JTextField availableConnectionsLabel = new JTextField("Available connections:");
    private JList<String> list;
    private DefaultListModel<String> listModel = new DefaultListModel<String>();

    private JTextField messageToSendLabel = new JTextField("Enter your message:");
    private JTextArea messageToSend = new JTextArea();
    private LoginButton sendButton = new LoginButton("SEND");

    private JTextField messageReceivedLabel = new JTextField("Chat Messages:");
    private JTextArea messageReceived = new JTextArea(10, 30);

    private DatagramSocket socket;
    private static final int serverPort = 5000;
    private InetAddress address;

    private String sendTo;

    private static ClientClass CLIENT;
    private NicknameFrame NicknameFrame;

    private Font arialLabels = new Font("Arial", Font.PLAIN, 20);
    private Font arialText = new Font("Arial", Font.PLAIN, 16);


    private ClientClass() throws IOException {
        super();
        //Starting with the Nickname frame
        NicknameFrame = new NicknameFrame("Client");
        NicknameFrame.setVisible(true);
    }

    //Initial frame to add the nickname to the name server
    private class NicknameFrame extends JFrame implements ActionListener {
        private JTextField nicknameLabel = new JTextField("Enter your nickname here:");
        private JTextField nicknameTextField = new JTextField("");
        private LoginButton buttonAddNewNickname = new LoginButton("START"); //Enter ChatApp

        private NicknameFrame(String name) {
            super(name);
            setNicknameFrameGUI();
        }

        private void setNicknameFrameGUI() {
            setBackground(Color.white.brighter());
            setSize(600, 200);
            setResizable(false);
            //to center in the screen:
            setLocationRelativeTo(null);

            nicknameLabel.setEditable(false);
            nicknameLabel.setHorizontalAlignment(JTextField.CENTER);
            nicknameLabel.setFont(arialLabels);
            nicknameLabel.setBackground(Colors.lightBlue);
            nicknameLabel.setForeground(Color.white);
            nicknameLabel.setBorder(null);

            nicknameTextField.setFont(arialLabels);
            nicknameTextField.setBackground(Colors.lightBlue);
            nicknameTextField.setForeground(Color.white);
            nicknameTextField.setBorder(new LineBorder(Color.white));


            buttonAddNewNickname.addActionListener(this);
            buttonAddNewNickname.setBackground(Colors.lightBlue);
            buttonAddNewNickname.setForeground(Color.white);
            buttonAddNewNickname.setFont(arialLabels);
            buttonAddNewNickname.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonAddNewNickname.setAlignmentY(Component.CENTER_ALIGNMENT);
            buttonAddNewNickname.setBorder(new LineBorder(Color.white));


            Panel buttonPanel = new Panel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            buttonPanel.add(buttonAddNewNickname);
            buttonPanel.setBackground(Colors.lightBlue);

            Panel panelNickname = new Panel() {
                public Insets getInsets() {
                    return new Insets(5, 5, 5, 50);
                }
            };
            panelNickname.setLayout(new GridLayout(0, 2, 0, 10));
            panelNickname.add(nicknameLabel);
            panelNickname.add(nicknameTextField);
            panelNickname.setBackground(Colors.lightBlue);

            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent event) {
                    warnServerLeavingApp();
                }
            });
            Panel nicknameAndButton = new Panel();
            //nicknameAndButton.setBackground(Colors.lightBlue);
            nicknameAndButton.setLayout(new GridLayout(2, 1));
            nicknameAndButton.add(panelNickname);
            nicknameAndButton.add(buttonPanel);
            add(nicknameAndButton);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == buttonAddNewNickname) {
                if (!nicknameTextField.getText().equals("")) {
                    // System.out.println("My name is " + nicknameTextField.getText());
                    sendDP(socket, serverPort, BIND + ":" + nicknameTextField.getText(), address.toString().substring(11));
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            CLIENT = new ClientClass();
            CLIENT.startClient();
            CLIENT.clientGUI();
            CLIENT.setLocationRelativeTo(null);
            CLIENT.setVisible(false);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void clientGUI() {
        setBackground(Color.white);
        setSize(700, 500);
        setResizable(false);

        availableConnectionsLabel.setEditable(false);
        availableConnectionsLabel.setHorizontalAlignment(JTextField.CENTER);
        availableConnectionsLabel.setFont(arialLabels);
        availableConnectionsLabel.setBackground(Colors.lightBlue);
        availableConnectionsLabel.setForeground(Color.white);
        availableConnectionsLabel.setBorder(null);

        messageToSendLabel.setEditable(false);
        messageToSendLabel.setFont(arialLabels);
        messageToSendLabel.setBackground(Colors.lightBlue);
        messageToSendLabel.setForeground(Color.white);
        messageToSendLabel.setBorder(null);

        messageToSend.setFont(arialText);

        sendButton.addActionListener(this);
        sendButton.setBackground(Colors.lightBlue);
        sendButton.setForeground(Color.white);
        sendButton.setBorder(new LineBorder(Color.white));

        messageReceivedLabel.setEditable(false);
        messageReceivedLabel.setFont(arialLabels);
        messageReceivedLabel.setBackground(Colors.lightBlue);
        messageReceivedLabel.setBorder(null);
        messageReceivedLabel.setForeground(Color.white);

        messageReceived.setEditable(false);
        messageReceived.setFont(arialText);


        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                warnServerLeavingApp();
            }
        });

        //setting the list that shows the available connections
        list = new JList<String>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.setVisibleRowCount(5);
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                sendTo = list.getModel().getElementAt(list.locationToIndex(e.getPoint()));
            }
        });

        //adding the ability to scroll the list
        listScrollConnections = new JScrollPane(list);
        list.setFont(arialText);
        JScrollPane sentMessagesScroll = new JScrollPane(messageToSend);
        JScrollPane receivedMessagesScroll = new JScrollPane(messageReceived);

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        //Container pane = CLIENT;
        Panel pane = new Panel();
        pane.setFont(new Font("Helvetica", Font.PLAIN, 14));
        pane.setLayout(gridbag);
        pane.setBackground(Colors.lightBlue);


        c.anchor = GridBagConstraints.FIRST_LINE_START;
        //buttons added to provide more height to the layout
        AuxiliaryBoxesUI.addVerticalboxes(pane, c);

        c.insets = new Insets(10, 0, 2, 20);
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridx = 1;
        pane.add(availableConnectionsLabel, c);

        c.insets = new Insets(0, 0, 10, 20);
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridx = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 7;
        pane.add(listScrollConnections, c);

        c.insets = new Insets(10, 0, 2, 30);
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridheight = 1;
        pane.add(messageToSendLabel, c);

        //buttons added in the top right corner so we can have more space to write the message
        AuxiliaryBoxesUI.addHorizontalBoxes(pane, c);

        c.insets = new Insets(0, 0, 2, 30);
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 1;
        c.gridx = 2;
        c.gridwidth = 3;
        c.gridheight = 6;
        pane.add(sentMessagesScroll, c);

        c.insets = new Insets(0, 0, 10, 30);
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 7;
        c.gridx = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        pane.add(sendButton, c);

        c.insets = new Insets(0, 0, 2, 10);
        c.gridy = 8;
        c.gridwidth = 1;
        c.gridx = 1;
        pane.add(messageReceivedLabel, c);

        c.insets = new Insets(0, 0, 10, 30);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridy = 9;
        c.gridx = 1;
        c.gridwidth = 4;
        c.gridheight = 8;
        pane.add(receivedMessagesScroll, c);
        add(pane);

    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == sendButton) {
            if (!messageToSend.getText().equals("")) {
                if (sendTo != null)
                    sendDP(socket, serverPort, SENDTO + ":" + sendTo.trim(), getThisClientAddressAsString());
                else
                    JOptionPane.showMessageDialog(CLIENT,
                            "Please select a name in the available connections.");
            }
        }
    }

    private String getThisClientAddressAsString() {
        return address.toString().substring(11);
    }

    //sending a message to the server that this client is leaving the chat app
    private void warnServerLeavingApp() {
        sendDP(socket, serverPort,
                LEAVING_CHAT + ":" + NicknameFrame.nicknameTextField.getText(),
                getThisClientAddressAsString());
        dispose();
        System.exit(0);
    }

    //method that starts the thread that 'listens' to incoming packets
    private void startClient() {
        RunnableClient newClient = new RunnableClient();
        newClient.start();
    }

    //Method to send package to server, other client, etc
    private void sendDP(DatagramSocket socket, int port, String msg, String end) {
        int length = msg.length();
        byte[] buffer = msg.getBytes();
        try {
            InetAddress inetAddress = InetAddress.getByName(end);
            DatagramPacket datagramPacket = new DatagramPacket(buffer,
                    length,
                    inetAddress,
                    port);
            socket.send(datagramPacket);

        } catch (Exception exception) {
            System.out.println("Error sending datagramPacket: " + exception);
            for (StackTraceElement each : exception.getStackTrace())
                System.out.println(each);
        }
    }

    private void sendEncryptedDP(DatagramSocket socket, int port, String end, byte[] encryptedMessage) {
        try {
            InetAddress inetAddress = InetAddress.getByName(end);
            DatagramPacket datagramPacket = new DatagramPacket(encryptedMessage,
                    encryptedMessage.length,
                    inetAddress,
                    port);
            socket.send(datagramPacket);
        } catch (Exception exception) {
            System.out.println("Error sending encrypted datagramPacket: " + exception);
            for (StackTraceElement each : exception.getStackTrace())
                System.out.println(each);
        }
    }

    public class RunnableClient extends Thread {
        public void run() {
            try {

                address = InetAddress.getLocalHost();
                socket = new DatagramSocket();//if we don't specify a port, a free one is used

                while (true) {

                    byte[] buffer2 = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer2, buffer2.length);
                    socket.receive(packet);
                    byte[] buffer = packet.getData();
                    int packetLength = packet.getLength();

                    String message = new String(buffer, 0, packetLength);
                    String code = message.split(":")[0];
                    System.out.println("Code received: " + code);

                    switch (code) {
                        case ALIVE_STRING:
                            System.out.println("im ALIVE");
                            sendDP(socket, serverPort, ALIVE_STRING + ":", getThisClientAddressAsString());
                            break;

                        case ADDRESS_STRING:
                            sendingMessageToAddress(message);
                            break;

                        case MESSAGE_STRING:
                            receivedMessage(message);
                            break;

                        case CONNECTIONS_STRING:
                            receivedConnectionsAvailable(message);
                            break;

                        case NICKNAME_NOT_AVAILABLE_STRING:
                            JOptionPane.showMessageDialog(CLIENT,
                                    "That nickname is already taken, please select another.");
                            break;

                        case NICKNAME_AVAILABLE_STRING:
                            NicknameFrame.setVisible(false);
                            CLIENT.setTitle(NicknameFrame.nicknameTextField.getText());
                            System.out.println("I'm " + NicknameFrame.nicknameTextField.getText());
                            CLIENT.setVisible(true);
                            break;

                        default:
                            receivedEncriptedMessage(buffer, packetLength);
                    }
                }
            } catch (SocketTimeoutException e) {
                System.out.println("The socket timed out");
            } catch (IOException e) {
                System.out.println("ClientClass error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Other exception: " + e.getMessage());
                for (StackTraceElement each : e.getStackTrace())
                    System.out.println(each);
            }
        }

        private void sendingMessageToAddress(String message) {
            //getting the nickname
            String myNickname = NicknameFrame.nicknameTextField.getText().trim();
            //collecting the message written by the user
            String onlyMessage = messageToSend.getText().trim();
            //message that we want to send (including code)
            String messageBeforeEncryption = MESSAGE_STRING + ":" + myNickname + ": " + onlyMessage;

            //encrypting message before sending it
            MessageEncoder encoder = new MessageEncoder();
            byte[] messageAfterEncryption = new byte[0];
            try {
                messageAfterEncryption = encoder.encodeMessage(messageBeforeEncryption);
            } catch (Exception except) {
                System.out.println(except.getMessage());
                for (StackTraceElement each : except.getStackTrace())
                    System.out.println(each);
            }
            System.out.println("message after encryption in the client: " + new String(messageAfterEncryption));

            //Parsing the received message from the server
            //to get the address and port of the person we want to send the message
            String codeAddress = ADDRESS_STRING + ":";
            String[] messageWithoutCode = message.split(codeAddress);
            String[] arrayWithAddressAndPort = messageWithoutCode[1].split(PORT_STRING);

            String addressOfReceiver = arrayWithAddressAndPort[0]
                    .split("/")[1];//regex to remove '/' from the address
            int portOfReceiver = Integer.valueOf(arrayWithAddressAndPort[1]);

            sendEncryptedDP(socket, portOfReceiver, addressOfReceiver, messageAfterEncryption);
            messageReceived.append("\n" + "Me: " + messageToSend.getText());

            //Adding color to the "Me:" string, to help with visibility
            String SEARCH_STRING = "Me: ";
            highlightingPersonTalking(SEARCH_STRING);
            messageToSend.setText("");
        }

        private void highlightingPersonTalking(String SEARCH_STRING) {

            final String SEARCH_STRING_ME = "Me: ";
            final Highlighter.HighlightPainter orangePainter = new DefaultHighlighter.DefaultHighlightPainter(Color.ORANGE);
            final Highlighter.HighlightPainter cPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.cyan);

            Highlighter h = messageReceived.getHighlighter();
            // pattern to compare \\b matches word boundaries
            Pattern pattern = Pattern.compile(SEARCH_STRING, Pattern.DOTALL | Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(messageReceived.getText());

            int position;
            int fromIndex = 0;
            //matcher.find() checks for all occurrences
            while (matcher.find()) {
                position = messageReceived.getText().indexOf(SEARCH_STRING, fromIndex);
                fromIndex = position + SEARCH_STRING.length();
                try {
                    if (SEARCH_STRING.equals(SEARCH_STRING_ME)) {
                        h.addHighlight(position,
                                position + SEARCH_STRING.split(" ")[0].length(),
                                orangePainter);
                    } else {
                        System.out.println("we're on else");
                        h.addHighlight(position,
                                position + SEARCH_STRING.length(),
                                cPainter);
                    }
                } catch (BadLocationException e) {
                    System.out.println("Cant highlight message writer's name");
                    e.printStackTrace();
                }
            }
        }

        private void receivedMessage(String message) throws Exception {
            System.out.println("GOT MESSAGE");
            String codeMessage = MESSAGE_STRING + ":";

            String[] removeCodeFromMessage = message.split(codeMessage);
            String messageWithoutCode = removeCodeFromMessage[1];

            messageReceived.append("\n" + messageWithoutCode);
            messageToSend.setText("");
        }

        private void receivedEncriptedMessage(byte[] encryptedMessage, int packageLength) {

            //removing extra bytes
            byte[] sizedDownArray = Arrays.copyOfRange(encryptedMessage, 0, packageLength);

            //Decrypting message
            MessageEncoder decoder = new MessageEncoder();
            String decryptedMessage = null;
            try {
                decryptedMessage = decoder.decodeMessage(sizedDownArray);
            } catch (Exception except) {
                System.out.println(except.getMessage());
                for (StackTraceElement each : except.getStackTrace())
                    System.out.println(each);
            }
            System.out.println("Decripted message on client: " + decryptedMessage);

            //parsing received message
            String codeMessage = MESSAGE_STRING + ":";
            if (decryptedMessage != null && decryptedMessage.split(":")[0].equals(MESSAGE_STRING)) {
                String messageWithoutCode = decryptedMessage.split(codeMessage)[1];
                messageReceived.append("\n" + messageWithoutCode);

                //and highlighting it
                String SEARCH_STRING = decryptedMessage.split(":")[1] + ":";
                System.out.println("search string: " + SEARCH_STRING);

                highlightingPersonTalking(SEARCH_STRING);

                messageToSend.setText("");
            }
        }

        private void receivedConnectionsAvailable(String message) {
            String codeConnections = CONNECTIONS_STRING + ":";
            String[] msgWithoutCode = message.split(codeConnections);
            String messageConnectionsReceived = msgWithoutCode[1];

            String[] listConnectAvail = messageConnectionsReceived.split(";");

            //Updating the list of available connections:
            if (messageConnectionsReceived.equals(NO_CONNECTIONS)) {
                listModel.removeAllElements();
                listModel.addElement(messageConnectionsReceived);
                list = new JList<String>(listModel);
                listScrollConnections.revalidate();
                listScrollConnections.repaint();

            } else {
                listModel.removeAllElements();
                for (String each : listConnectAvail) {
                    listModel.addElement(each);
                }
                list = new JList<String>(listModel);
                listScrollConnections.revalidate();
                listScrollConnections.repaint();
            }
        }
    }
}