package Home;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import com.toedter.calendar.*;
import java.beans.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import java.text.*;
import javax.swing.filechooser.*;
import java.awt.print.*;
public class home extends JFrame{
    PhotoPanel photoPanel = new PhotoPanel();
    JButton newb, print, save, nextb, prev, exitb, del;
    JToolBar inventory;
    JLabel itemLabel, locationLabel, serialLabel;
    JTextField itemText, serialText;
    JComboBox locationComboBox;
    JCheckBox markedCheckBox;
    GridBagConstraints grid;
    JLabel priceLabel = new JLabel();
    JTextField priceTextField = new JTextField();
    JLabel dateLabel = new JLabel();
    JDateChooser dateDateChooser = new JDateChooser();
    JLabel storeLabel = new JLabel();
    JTextField storeTextField = new JTextField();
    JLabel noteLabel = new JLabel();
    JTextField noteTextField = new JTextField();
    JLabel photoLabel = new JLabel();
    JButton photoButton = new JButton();
    JPanel searchPanel = new JPanel();
    JButton[] searchButton = new JButton[26];
    static final int maximumEntries = 300;
    static InventoryItem[] myInventory = new InventoryItem[maximumEntries];
    int currentEntry;
    
    static JTextArea photoTextArea = new JTextArea();
    static int lastPage;
    static final int entriesPerPage = 2;
    static int numberEntries;
    class PhotoPanel extends JPanel
    {
        public void paintComponent(Graphics g){
            Graphics2D g2D = (Graphics2D) g;
            super.paintComponent(g2D);
            g2D.setPaint(Color.BLACK);
            g2D.draw(new Rectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1));
            Image photoImage = new ImageIcon(home.photoTextArea.getText()).getImage( );
            int w = getWidth();
            int h = getHeight();
            double rWidth = (double) getWidth() / (double) photoImage.getWidth(null);
            double rHeight = (double) getHeight() / (double) photoImage.getHeight(null);
            if (rWidth > rHeight){
                w = (int) (photoImage.getWidth(null) * rHeight);
            }
            else{
                h = (int) (photoImage.getHeight(null) * rWidth);
            }
            g2D.drawImage(photoImage, (int) (0.5 * (getWidth() - w)), (int) (0.5 * (getHeight() - h)), w, h, null);

            g2D.dispose();
        }
    }
    
    class InventoryDocument implements Printable{
        public int print(Graphics g, PageFormat pf, int pageIndex)
        {
            Graphics2D g2D = (Graphics2D) g;
            if ((pageIndex + 1) > home.lastPage)
            {
                return NO_SUCH_PAGE;
            }
            int i, iEnd;
            g2D.setFont(new Font("Arial", Font.BOLD, 14));
            g2D.drawString("Home Inventory Items - Page " + String.valueOf(pageIndex + 1), (int) pf.getImageableX(), (int) (pf.getImageableY() + 25));
            int dy = (int) g2D.getFont().getStringBounds("S", g2D.getFontRenderContext()).getHeight();
            int y = (int) (pf.getImageableY() + 4 * dy);
            iEnd = home.entriesPerPage * (pageIndex + 1);
            if (iEnd > home.numberEntries)
            iEnd = home.numberEntries;
            for (i = 0 + home.entriesPerPage * pageIndex; i < iEnd; i++)
            {
                Line2D.Double dividingLine = new Line2D.Double(pf.getImageableX(), y,pf.getImageableX() + pf.getImageableWidth(), y);
                g2D.draw(dividingLine);
                y += dy;
                g2D.setFont(new Font("Arial", Font.BOLD, 12));
                g2D.drawString(home.myInventory[i].description, (int) pf.getImageableX(), y);
                y += dy;
                g2D.setFont(new Font("Arial", Font.PLAIN, 12));
                g2D.drawString("Location: " + home.myInventory[i].location, (int)(pf.getImageableX() + 25), y);
                y += dy;
                if (home.myInventory[i].marked)
                g2D.drawString("Item is marked with identifying information.", (int)(pf.getImageableX() + 25), y);
                else
                g2D.drawString("Item is NOT marked with identifying information.", (int)(pf.getImageableX() + 25), y);
                y += dy;
                g2D.drawString("Serial Number: " + home.myInventory[i].serialNumber, (int) (pf.getImageableX() + 25), y);
                y += dy;
                g2D.drawString("Price: $" + home.myInventory[i].purchasePrice + ",Purchased on: " + 
                home.myInventory[i].purchaseDate, (int) (pf.getImageableX() + 25), y);
                y += dy;
                g2D.drawString("Purchased at: " + home.myInventory[i].purchaseLocation, (int) (pf.getImageableX() + 25), y);
                y += dy;
                g2D.drawString("Note: " + home.myInventory[i].note, (int)(pf.getImageableX() + 25), y);
                y += dy;
                try
                {
                    Image inventoryImage = new ImageIcon(home.myInventory[i].photoFile).getImage ();
                    double ratio = (double) (inventoryImage.getWidth(null)) / (double)inventoryImage.getHeight(null);
                    g2D.drawImage(inventoryImage, (int) (pf.getImageableX() + 25), y, (int) (100 * ratio), 100, null);
                }
                catch (Exception ex)
                {
                }
                y += 2 * dy + 100;
            }
            return PAGE_EXISTS;
        }
    }

    public home(){
        setTitle("HomeInventory");
        setVisible(true);
        setSize(800,800);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        inventory = new JToolBar();
        newb = new JButton(new ImageIcon("C:\\Users\\PC-LENOVO\\Desktop\\java\\new1.png"));
        del = new JButton(new ImageIcon("C:\\Users\\PC-LENOVO\\Desktop\\java\\delete1.png"));
        save = new JButton(new ImageIcon("C:\\Users\\PC-LENOVO\\Desktop\\java\\save1.png"));
        nextb = new JButton(new ImageIcon("C:\\Users\\PC-LENOVO\\Desktop\\java\\next1.png"));
        prev = new JButton(new ImageIcon("C:\\Users\\PC-LENOVO\\Desktop\\java\\prev1.png"));
        print = new JButton(new ImageIcon("C:\\Users\\PC-LENOVO\\Desktop\\java\\print1.png"));
        exitb = new JButton();
        itemLabel = new JLabel();
        locationLabel = new JLabel();
        serialLabel = new JLabel();
        itemText = new JTextField();
        serialText = new JTextField();
        locationComboBox = new JComboBox();
        markedCheckBox = new JCheckBox();
        
        inventory.setFloatable(false);
        inventory.setBackground(Color.BLUE);
        inventory.setOrientation(SwingConstants.VERTICAL);
        grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.gridheight = 0;
        grid.fill = GridBagConstraints.VERTICAL;
        getContentPane().add(inventory,grid);
        
        inventory.addSeparator();
        
        Dimension bSize = new Dimension(70,50);
        newb.setText("New");
        sizeButton(newb, bSize);
        newb.setToolTipText("Add New Item");
        newb.setHorizontalTextPosition(SwingConstants.CENTER);
        newb.setVerticalTextPosition(SwingConstants.BOTTOM);
        inventory.add(newb);
        newb.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                newbActionPerformed(e);
            }
        });
        
        del.setText("Delete");
        sizeButton(del, bSize);
        del.setToolTipText("Delete Current Item");
        del.setHorizontalTextPosition(SwingConstants.CENTER);
        del.setVerticalTextPosition(SwingConstants.BOTTOM);
        inventory.add(del);
        del.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                delActionPerformed(e);
            }
        });
        
        save.setText("Save");
        sizeButton(save, bSize);
        save.setToolTipText("Save Item");
        save.setHorizontalTextPosition(SwingConstants.CENTER);
        save.setVerticalTextPosition(SwingConstants.BOTTOM);
        inventory.add(save);
        save.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                saveActionPerformed(e);
            }
        });
        
        inventory.addSeparator();
        
        prev.setText("Previous");
        sizeButton(prev, bSize);
        prev.setToolTipText("Previous Item");
        prev.setHorizontalTextPosition(SwingConstants.CENTER);
        prev.setVerticalTextPosition(SwingConstants.BOTTOM);
        inventory.add(prev);
        prev.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                prevActionPerformed(e);
            }
        });
        
        nextb.setText("Next");
        sizeButton(nextb, bSize);
        nextb.setToolTipText("Display Next Item");
        nextb.setHorizontalTextPosition(SwingConstants.CENTER);
        nextb.setVerticalTextPosition(SwingConstants.BOTTOM);
        inventory.add(nextb);
        nextb.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                nextbActionPerformed(e);
            }
        });
        
        inventory.addSeparator();
        
        print.setText("Print");
        sizeButton(print, bSize);
        print.setToolTipText("Print Inventory List");
        print.setHorizontalTextPosition(SwingConstants.CENTER);
        print.setVerticalTextPosition(SwingConstants.BOTTOM);
        inventory.add(print);
        print.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                printActionPerformed(e);
            }
        });
        
        exitb.setText("Exit");
        sizeButton(exitb, bSize);
        exitb.setToolTipText("Exit Program");
        inventory.add(exitb);
        exitb.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                exitbActionPerformed(e);
            }
        });
        
        itemLabel.setText("Inventory Item");
        grid = new GridBagConstraints();
        grid.gridx = 1;
        grid.gridy = 0;
        grid.insets = new Insets(10,10,0,10);
        grid.anchor = GridBagConstraints.EAST;
        getContentPane().add(itemLabel, grid);
        
        itemText.setPreferredSize(new Dimension(400, 25));
        grid = new GridBagConstraints();
        grid.gridx = 2;
        grid.gridy = 0;
        grid.gridwidth = 5;
        grid.insets = new Insets(10, 0, 0, 10);
        grid.anchor = GridBagConstraints.WEST;
        getContentPane().add(itemText, grid);
        itemText.addActionListener(new ActionListener ()
        {
            public void actionPerformed(ActionEvent e)
            {
                itemTextActionPerformed(e);
            }
        });
        
        locationLabel.setText("Location");
        grid = new GridBagConstraints();
        grid.gridx = 1;
        grid.gridy = 1;
        grid.insets = new Insets(10, 10, 0, 10);
        grid.anchor = GridBagConstraints.EAST;
        getContentPane().add(locationLabel, grid);
        locationComboBox.setPreferredSize(new Dimension(270, 25));
        locationComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        locationComboBox.setEditable(true);
        locationComboBox.setBackground(Color.WHITE);
        grid = new GridBagConstraints();
        grid.gridx = 2;
        grid.gridy = 1;
        grid.gridwidth = 3;
        grid.insets = new Insets(10, 0, 0, 10);
        grid.anchor = GridBagConstraints.WEST;
        getContentPane().add(locationComboBox, grid);
        locationComboBox.addActionListener(new ActionListener ()
        {
            public void actionPerformed(ActionEvent e)
            {
                locationComboBoxActionPerformed(e);
            }
        });
        
        markedCheckBox.setText("Marked?");
        grid = new GridBagConstraints();
        grid.gridx = 5;
        grid.gridy = 1;
        grid.insets = new Insets(10, 10, 0, 0);
        grid.anchor = GridBagConstraints.WEST;
        getContentPane().add(markedCheckBox,grid);
        
        serialLabel.setText("Serial Number");
        grid = new GridBagConstraints();
        grid.gridx = 1;
        grid.gridy = 2;
        grid.insets = new Insets(10, 10, 0, 10);
        grid.anchor = GridBagConstraints.EAST;
        getContentPane().add(serialLabel, grid);
        
        serialText.setPreferredSize(new Dimension(270, 25));
        grid = new GridBagConstraints();
        grid.gridx = 2;
        grid.gridy = 2;
        grid.gridwidth = 3;
        grid.insets = new Insets(10, 0, 0, 10);
        grid.anchor = GridBagConstraints.WEST;
        getContentPane().add(serialText, grid);
        
        priceLabel.setText("Purchase Price");
        grid = new GridBagConstraints();
        grid.gridx = 1;
        grid.gridy = 3;
        grid.insets = new Insets(10, 10, 0, 10);
        grid.anchor = GridBagConstraints.EAST;
        getContentPane().add(priceLabel, grid);
        
        priceTextField.setPreferredSize(new Dimension(160, 25));
        grid = new GridBagConstraints();
        grid.gridx = 2;
        grid.gridy = 3;
        grid.gridwidth = 2;
        grid.insets = new Insets(10, 0, 0, 10);
        grid.anchor = GridBagConstraints.WEST;
        getContentPane().add(priceTextField, grid);
        priceTextField.addActionListener(new ActionListener ()
        {
            public void actionPerformed(ActionEvent e)
            {
                priceTextFieldActionPerformed(e);
            }
        });
        
        dateLabel.setText("Date Purchased");
        grid = new GridBagConstraints();
        grid.gridx = 4;
        grid.gridy = 3;
        grid.insets = new Insets(10, 10, 0, 0);
        grid.anchor = GridBagConstraints.WEST;
        getContentPane().add(dateLabel, grid);
        dateDateChooser.setPreferredSize(new Dimension(120, 25));
        grid = new GridBagConstraints();
        grid.gridx = 5;
        grid.gridy = 3;
        grid.gridwidth = 2;
        grid.insets = new Insets(10, 0, 0, 10);
        grid.anchor = GridBagConstraints.WEST;
        getContentPane().add(dateDateChooser, grid);
        dateDateChooser.addPropertyChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent e){
                dateDateChooserPropertyChange(e);
            }
        });
        
        storeLabel.setText("Store/Website");
        grid = new GridBagConstraints();
        grid.gridx = 1;
        grid.gridy = 4;
        grid.insets = new Insets(10, 10, 0, 10);
        grid.anchor = GridBagConstraints.EAST;
        getContentPane().add(storeLabel, grid);
        
        storeTextField.setPreferredSize(new Dimension(400, 25));
        grid = new GridBagConstraints();
        grid.gridx = 2;
        grid.gridy = 4;
        grid.gridwidth = 5;
        grid.insets = new Insets(10, 0, 0, 10);
        grid.anchor = GridBagConstraints.WEST;
        getContentPane().add(storeTextField, grid);
        storeTextField.addActionListener(new ActionListener ()
        {
            public void actionPerformed(ActionEvent e)
            {
                storeTextFieldActionPerformed(e);
            }
        });
        
        noteLabel.setText("Note");
        grid = new GridBagConstraints();
        grid.gridx = 1;
        grid.gridy = 5;
        grid.insets = new Insets(10, 10, 0, 10);
        grid.anchor = GridBagConstraints.EAST;
        getContentPane().add(noteLabel, grid);
        
        noteTextField.setPreferredSize(new Dimension(400, 25));
        grid = new GridBagConstraints();
        grid.gridx = 2;
        grid.gridy = 5;
        grid.gridwidth = 5;
        grid.insets = new Insets(10, 0, 0, 10);
        grid.anchor = GridBagConstraints.WEST;
        getContentPane().add(noteTextField, grid);
        noteTextField.addActionListener(new ActionListener ()
        {
            public void actionPerformed(ActionEvent e)
            {
                noteTextFieldActionPerformed(e);
            }
        });
        
        photoLabel.setText("Photo");
        grid = new GridBagConstraints();
        grid.gridx = 1;
        grid.gridy = 6;
        grid.insets = new Insets(10, 10, 0, 10);
        grid.anchor = GridBagConstraints.EAST;
        getContentPane().add(photoLabel, grid);
        
        photoTextArea.setPreferredSize(new Dimension(350, 35));
        photoTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        photoTextArea.setEditable(false);
        photoTextArea.setLineWrap(true);
        photoTextArea.setWrapStyleWord(true);
        photoTextArea.setBackground(new Color(255, 255, 192));
        photoTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        grid = new GridBagConstraints();
        grid.gridx = 2;
        grid.gridy = 6;
        grid.gridwidth = 4;
        grid.insets = new Insets(10, 0, 0, 10);
        grid.anchor = GridBagConstraints.WEST;
        getContentPane().add(photoTextArea, grid);
        photoButton.setText("...");
        grid = new GridBagConstraints();
        grid.gridx = 6;
        grid.gridy = 6;
        grid.insets = new Insets(10, 0, 0, 10);
        grid.anchor = GridBagConstraints.WEST;
        getContentPane().add(photoButton, grid);
        photoButton.addActionListener(new ActionListener (){
            public void actionPerformed(ActionEvent e)
            {
                photoButtonActionPerformed(e);
            }
        });
        
        searchPanel.setPreferredSize(new Dimension(240, 160));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Item Search"));
        searchPanel.setLayout(new GridBagLayout());
        grid = new GridBagConstraints();
        grid.gridx = 1;
        grid.gridy = 7;
        grid.gridwidth = 3;
        grid.insets = new Insets(10, 0, 10, 0);
        grid.anchor = GridBagConstraints.CENTER;
        getContentPane().add(searchPanel, grid);
        int x = 0, y = 0;
        for (int i = 0; i < 26; i++){
            searchButton[i] = new JButton();
            searchButton[i].setText(String.valueOf((char) (65 + i)));
            searchButton[i].setFont(new Font("Arial", Font.BOLD, 12));
            searchButton[i].setMargin(new Insets(-10, -10, -10, -10));
            sizeButton(searchButton[i], new Dimension(37, 27));
            searchButton[i].setBackground(Color.YELLOW);
            grid = new GridBagConstraints();
            grid.gridx = x;
            grid.gridy = y;
            searchPanel.add(searchButton[i], grid);
            searchButton[i].addActionListener(new ActionListener ()
            {
                public void actionPerformed(ActionEvent e)
                {
                    searchButtonActionPerformed(e);
                }
            });
            x++;
            if (x % 6 == 0){
                x = 0;
                y++;
            }
        }

        photoPanel.setPreferredSize(new Dimension(240, 160));
        grid = new GridBagConstraints();
        grid.gridx = 4;
        grid.gridy = 7;
        grid.gridwidth = 3;
        grid.insets = new Insets(10, 0, 10, 10);
        grid.anchor = GridBagConstraints.CENTER;
        getContentPane().add(photoPanel, grid);

        
        
        int n;
        try
        {
            BufferedReader inputFile = new BufferedReader(new FileReader("C:\\Users\\PC-LENOVO\\HomeInventory\\Home\\inventory.txt"));
            numberEntries = Integer.valueOf(inputFile.readLine()).intValue();
            if (numberEntries != 0){
                for (int i = 0; i < numberEntries; i++){
                    myInventory[i] = new InventoryItem();
                    myInventory[i].description = inputFile.readLine();
                    myInventory[i].location = inputFile.readLine();
                    myInventory[i].serialNumber = inputFile.readLine();
                    myInventory[i].marked = Boolean.valueOf(inputFile.readLine()).booleanValue();
                    myInventory[i].purchasePrice = inputFile.readLine();
                    myInventory[i].purchaseDate = inputFile.readLine();
                    myInventory[i].purchaseLocation = inputFile.readLine();
                    myInventory[i].note = inputFile.readLine();
                    myInventory[i].photoFile = inputFile.readLine();
                }
            }
            n = Integer.valueOf(inputFile.readLine()).intValue();
            if (n != 0){
                for (int i = 0; i < n; i++){
                    locationComboBox.addItem(inputFile.readLine());
                }
            }
            inputFile.close();
            currentEntry = 1;
            showEntry(currentEntry);
        }
        catch (Exception ex)
        {
            numberEntries = 0;
            currentEntry = 0;
        }
        if (numberEntries == 0){
            newb.setEnabled(false);
            del.setEnabled(false);
            nextb.setEnabled(false);
            prev.setEnabled(false);
            print.setEnabled(false);
        }
    }
    private void sizeButton(JButton b, Dimension d){
        b.setPreferredSize(d);
        b.setMinimumSize(d);
        b.setMaximumSize(d);
    }
    private void showEntry(int j){
        itemText.setText(myInventory[j - 1].description);
        locationComboBox.setSelectedItem(myInventory[j - 1].location);
        markedCheckBox.setSelected(myInventory[j - 1].marked);
        serialText.setText(myInventory[j - 1].serialNumber);
        priceTextField.setText(myInventory[j - 1].purchasePrice);
        dateDateChooser.setDate(stringToDate(myInventory[j - 1].purchaseDate));
        storeTextField.setText(myInventory[j - 1].purchaseLocation);
        noteTextField.setText(myInventory[j - 1].note);
        showPhoto(myInventory[j - 1].photoFile);
        nextb.setEnabled(true);
        prev.setEnabled(true);
        if (j == 1)
        prev.setEnabled(false);
        if (j == numberEntries)
        nextb.setEnabled(false);
        itemText.requestFocus();
    }

    private Date stringToDate(String s){
        int m = Integer.valueOf(s.substring(0, 2)).intValue() - 1;
        int d = Integer.valueOf(s.substring(3, 5)).intValue();
        int y = Integer.valueOf(s.substring(6)).intValue() - 1900;
        return(new Date(y, m, d));
    }

    private String dateToString(Date dd)
    {
        String yString = String.valueOf(dd.getYear() + 1900);
        int m = dd.getMonth() + 1;
        String mString = new DecimalFormat("00").format(m);
        int d = dd.getDate();
        String dString = new DecimalFormat("00").format(d);
        return(mString + "/" + dString + "/" + yString);
    }

    private void showPhoto(String photoFile){
        if (!photoFile.equals("")){
            try{
                photoTextArea.setText(photoFile);
            }
            catch (Exception ex){
                photoTextArea.setText("");
            }
        }
        else{
            photoTextArea.setText("");
        }
        photoPanel.repaint();
    }
    private void exitForm(WindowEvent evt){
        if (JOptionPane.showConfirmDialog(null, "Any unsaved changes will be lost.\nAre you sure you want to exit?",
        "Exit Program", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
        return;

        try{
            PrintWriter outputFile = new PrintWriter(new BufferedWriter(new
            FileWriter("inventory.txt")));
            outputFile.println(numberEntries);
            if (numberEntries != 0)
            {
                for (int i = 0; i < numberEntries; i++)
                {
                    outputFile.println(myInventory[i].description);
                    outputFile.println(myInventory[i].location);
                    outputFile.println(myInventory[i].serialNumber);
                    outputFile.println(myInventory[i].marked);
                    outputFile.println(myInventory[i].purchasePrice);
                    outputFile.println(myInventory[i].purchaseDate);
                    outputFile.println(myInventory[i].purchaseLocation);
                    outputFile.println(myInventory[i].note);
                    outputFile.println(myInventory[i].photoFile);
                }
            }
            outputFile.println(locationComboBox.getItemCount());
            if (locationComboBox.getItemCount() != 0)
            {
                for (int i = 0; i < locationComboBox.getItemCount(); i++)
                outputFile.println(locationComboBox.getItemAt(i));}
                outputFile.close();
            }
            catch (Exception ex)
            {
            }
            System.exit(0);
        }

    private void newbActionPerformed(ActionEvent e){
        checkSave();
        blankValues();
    }
    private void delActionPerformed(ActionEvent e){
        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this item?","Delete Inventory Item", JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
        return;
        deleteEntry(currentEntry);
        if (numberEntries == 0)
        {
            currentEntry = 0;
            blankValues();
        }
        else
        {
            currentEntry--;
            if (currentEntry == 0)
            currentEntry = 1;
            showEntry(currentEntry);
        }
    }
    private void saveActionPerformed(ActionEvent e){
        itemText.setText(itemText.getText().trim());
        if (itemText.getText().equals("")){
            JOptionPane.showConfirmDialog(null, "Must have item description.", "Error",
            JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            itemText.requestFocus();
            return;
        }
        if (newb.isEnabled())
        {
            deleteEntry(currentEntry);
        }
        String s = itemText.getText();
        itemText.setText(s.substring(0, 1).toUpperCase() + s.substring(1));
        numberEntries++;
        currentEntry = 1;
        if (numberEntries != 1)
        {
            do
            {
                if(itemText.getText().compareTo(myInventory[currentEntry - 1].description) < 0)
                break;
                currentEntry++;
            }
            while (currentEntry < numberEntries);
        }
        if (currentEntry != numberEntries)
        {
            for (int i = numberEntries; i >= currentEntry + 1; i--)
            {
                myInventory[i - 1] = myInventory[i - 2];
                myInventory[i - 2] = new InventoryItem();}
            }
            myInventory[currentEntry - 1] = new InventoryItem();
            myInventory[currentEntry - 1].description = itemText.getText();
            myInventory[currentEntry - 1].location =
            locationComboBox.getSelectedItem().toString();
            myInventory[currentEntry - 1].marked = markedCheckBox.isSelected();
            myInventory[currentEntry - 1].serialNumber = serialText.getText();
            myInventory[currentEntry - 1].purchasePrice = priceTextField.getText();
            myInventory[currentEntry - 1].purchaseDate = dateToString(dateDateChooser.getDate());
            myInventory[currentEntry - 1].purchaseLocation = storeTextField.getText();
            myInventory[currentEntry - 1].photoFile = photoTextArea.getText();
            myInventory[currentEntry - 1].note = noteTextField.getText();
            showEntry(currentEntry);
            if (numberEntries < maximumEntries)
            newb.setEnabled(true);
            else
            newb.setEnabled(false);
            del.setEnabled(true);
            print.setEnabled(true);
    }
    private void prevActionPerformed(ActionEvent e){
        checkSave();
        currentEntry--;
        showEntry(currentEntry);
    }
    private void nextbActionPerformed(ActionEvent e){
        checkSave();
        currentEntry++;
        showEntry(currentEntry);
    }
    private void printActionPerformed(ActionEvent e){
        lastPage = (int) (1 + (numberEntries - 1) / entriesPerPage);
        PrinterJob inventoryPrinterJob = PrinterJob.getPrinterJob();
        inventoryPrinterJob.setPrintable(new InventoryDocument());
        if (inventoryPrinterJob.printDialog())
        {
            try
            {
                inventoryPrinterJob.print();
            }
            catch (PrinterException ex)
            {
                JOptionPane.showConfirmDialog(null, ex.getMessage(), "Print Error",
                JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void exitbActionPerformed(ActionEvent e){
        exitForm(null);
    }
    private void photoButtonActionPerformed(ActionEvent e)
    {
        JFileChooser openChooser = new JFileChooser();
        openChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        openChooser.setDialogTitle("Open Photo File");
        openChooser.addChoosableFileFilter(new FileNameExtensionFilter("Photo Files","jpg"));
        if (openChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        showPhoto(openChooser.getSelectedFile().toString());
    }
    private void searchButtonActionPerformed(ActionEvent e){
        int i;
        if (numberEntries == 0)
        return;
        String letterClicked = e.getActionCommand();
        i = 0;
        do
        {
            if (myInventory[i].description.substring(0, 1).equals(letterClicked))
            {
                currentEntry = i + 1;
                showEntry(currentEntry);
                return;
            }
            i++;
        }
        while (i < numberEntries);
        JOptionPane.showConfirmDialog(null, "No " + letterClicked + " inventory items.","None Found", JOptionPane.DEFAULT_OPTION,
        JOptionPane.INFORMATION_MESSAGE);
    }
    private void itemTextActionPerformed(ActionEvent e)
    {
        locationComboBox.requestFocus();
    }
    private void locationComboBoxActionPerformed(ActionEvent e)
    {
        if (locationComboBox.getItemCount() != 0){
            for (int i = 0; i < locationComboBox.getItemCount(); i++)
            {
                if(locationComboBox.getSelectedItem().toString().equals(locationComboBox.getItemAt(i).toString()))
                {
                    serialText.requestFocus();
                    return;
                }
            }
        }
        locationComboBox.addItem(locationComboBox.getSelectedItem( ));
        serialText.requestFocus();
    }
    private void priceTextFieldActionPerformed(ActionEvent e)
    {
        dateDateChooser.requestFocus();
    }
    private void dateDateChooserPropertyChange(PropertyChangeEvent e){
        storeTextField.requestFocus();
    }
    private void storeTextFieldActionPerformed(ActionEvent e)
    {
        noteTextField.requestFocus();
    }
    private void noteTextFieldActionPerformed(ActionEvent e)
    {
        photoButton.requestFocus();
    }
    private void blankValues(){
        newb.setEnabled(false);
        del.setEnabled(false);
        save.setEnabled(true);
        prev.setEnabled(false);
        nextb.setEnabled(false);
        print.setEnabled(false);
        itemText.setText("");
        locationComboBox.setSelectedItem("");
        markedCheckBox.setSelected(false);
        serialText.setText("");
        priceTextField.setText("");
        dateDateChooser.setDate(new Date());
        storeTextField.setText("");
        noteTextField.setText("");
        photoTextArea.setText("");
        photoPanel.repaint();
        itemText.requestFocus();
    }
    private void deleteEntry(int j){
        if (j != numberEntries)
        {
            for (int i = j; i < numberEntries; i++)
            {
                myInventory[i - 1] = new InventoryItem();
                myInventory[i - 1] = myInventory[i];
            }
        }
        numberEntries--;
    }
    private void checkSave()
    {
        boolean edited = false;
        if (!myInventory[currentEntry - 1].description.equals(itemText.getText()))
        edited = true;
        else if (!myInventory[currentEntry - 1].location.equals(locationComboBox.getSelectedItem().toString()))
        edited = true;
        else if (myInventory[currentEntry - 1].marked != markedCheckBox.isSelected())
        edited = true;
        else if (!myInventory[currentEntry - 1].serialNumber.equals(serialText.getText()))
        edited = true;
        else if (!myInventory[currentEntry - 1].purchasePrice.equals(priceTextField.getText()))
        edited = true;
        else if (!myInventory[currentEntry - 1].purchaseDate.equals(dateToString(dateDateChooser.getDate())))
        edited = true;
        else if (!myInventory[currentEntry - 1].purchaseLocation.equals(storeTextField.getText()))
        edited = true;
        else if (!myInventory[currentEntry - 1].note.equals(noteTextField.getText()))
        edited = true;
        else if (!myInventory[currentEntry - 1].photoFile.equals(photoTextArea.getText()))
        edited = true;
        if (edited)
        {
            if (JOptionPane.showConfirmDialog(null, "You have edited this item. Do you want to save the changes?", "Save Item", JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
            save.doClick();
        }
    }
    
    public static void main(String[]args){
        new home().show();
    }
}