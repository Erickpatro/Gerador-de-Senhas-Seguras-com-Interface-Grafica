import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.security.SecureRandom;

public class SecurePasswordGenerator extends JFrame {

    private JSpinner lengthSpinner;
    private JCheckBox upperCaseBox, lowerCaseBox, numbersBox, symbolsBox;
    private JTextField resultField;
    private JLabel strengthLabel;

    public SecurePasswordGenerator() {
        setTitle("🔒 Gerador de Senhas Seguras");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 1, 5, 5));
        setLocationRelativeTo(null);

        add(new JLabel("Tamanho da Senha:", SwingConstants.CENTER));
        lengthSpinner = new JSpinner(new SpinnerNumberModel(12, 4, 64, 1));
        add(lengthSpinner);

        upperCaseBox = new JCheckBox("Incluir Letras Maiúsculas (A-Z)", true);
        lowerCaseBox = new JCheckBox("Incluir Letras Minúsculas (a-z)", true);
        numbersBox = new JCheckBox("Incluir Números (0-9)", true);
        symbolsBox = new JCheckBox("Incluir Símbolos (!@#$%)", true);

        add(upperCaseBox);
        add(lowerCaseBox);
        add(numbersBox);
        add(symbolsBox);

        JButton generateButton = new JButton("Gerar Senha");
        generateButton.addActionListener(e -> generatePassword());
        add(generateButton);

        resultField = new JTextField();
        resultField.setHorizontalAlignment(JTextField.CENTER);
        resultField.setEditable(false);
        add(resultField);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        JButton copyButton = new JButton("Copiar Senha");
        copyButton.addActionListener(e -> copyToClipboard(resultField.getText()));
        bottomPanel.add(copyButton);

        strengthLabel = new JLabel("Força: -", SwingConstants.CENTER);
        bottomPanel.add(strengthLabel);

        add(bottomPanel);

        setVisible(true);
    }

    private void generatePassword() {
        int length = (int) lengthSpinner.getValue();

        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*()-_=+[]{};:,.<>?";

        StringBuilder allChars = new StringBuilder();
        if (upperCaseBox.isSelected()) allChars.append(upper);
        if (lowerCaseBox.isSelected()) allChars.append(lower);
        if (numbersBox.isSelected()) allChars.append(numbers);
        if (symbolsBox.isSelected()) allChars.append(symbols);

        if (allChars.length() == 0) {
            JOptionPane.showMessageDialog(this, "Selecione ao menos uma opção!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allChars.length());
            password.append(allChars.charAt(index));
        }

        resultField.setText(password.toString());
        updateStrengthLabel(password.toString());
    }

    private void updateStrengthLabel(String password) {
        int score = 0;
        if (password.matches(".*[A-Z].*")) score++;
        if (password.matches(".*[a-z].*")) score++;
        if (password.matches(".*[0-9].*")) score++;
        if (password.matches(".*[!@#$%^&*()_+\\-=[\\]{};':\",.<>/?].*")) score++;
        if (password.length() >= 12) score++;

        String strength;
        switch (score) {
            case 5 -> strength = "Muito Forte 💪";
            case 4 -> strength = "Forte 🔥";
            case 3 -> strength = "Média ⚙️";
            case 2 -> strength = "Fraca ⚠️";
            default -> strength = "Muito Fraca ❌";
        }

        strengthLabel.setText("Força: " + strength);
    }

    private void copyToClipboard(String text) {
        if (text.isEmpty()) return;
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(text), null);
        JOptionPane.showMessageDialog(this, "Senha copiada para a área de transferência!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SecurePasswordGenerator::new);
    }
}
