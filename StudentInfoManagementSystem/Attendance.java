
import java.awt.Image;
import java.awt.print.PrinterException;
import java.sql.SQLException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


/**
 *
 * @author pavan kumar
 */
public class Attendance extends javax.swing.JFrame {

    /**
     * Creates new form Attendance
     */
    static Connection conn;
    String todaydate;
    int count;
    LocalDate d;
    DateTimeFormatter dtf;

    public Attendance() throws SQLException {
        initComponents();
        try {
            ImageIcon icon = new ImageIcon("C:\\Program Files\\ProjectAttendance\\StudentWhite.png");
            setIconImage(icon.getImage());

            d = LocalDate.now();
            dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            todaydate = d.format(dtf);
            date.setText(todaydate);

            ImageIcon amritasai = new ImageIcon("C:\\Program Files\\ProjectAttendance\\attendance.jpg");
            Image img1 = amritasai.getImage();
            img1 = img1.getScaledInstance(amritaSaiImage.getWidth(), amritaSaiImage.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon i = new ImageIcon(img1);
            amritaSaiImage.setIcon(i);

            DataBaseConnection dbc = new DataBaseConnection();
            conn = dbc.getCon();

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    JLabel label, label1, label2;
    List<JCheckBox> checkboxes = new ArrayList<>();
    List<JLabel> snosList = new ArrayList<>();
    List<JLabel> regdsList = new ArrayList<>();
    List<JLabel> namsList = new ArrayList<>();
    List<String> oldAttendance = new ArrayList<>();
    List<String> newAttendance = new ArrayList<>();

    public String capitalizeWords(String name) {
        StringBuilder result = new StringBuilder();
        boolean b = true;
        for (char c : name.toCharArray()) {
            if (Character.isWhitespace(c)) {
                b = true;
                result.append(c);
            } else if (b) {
                result.append(Character.toUpperCase(c));
                b = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }
        return result.toString();
    }

    public void getData() {
        try {
            boolean columnExists = false;
            sno.removeAll();
            name.removeAll();
            regd.removeAll();
            abp.removeAll();
            String query = "select * from attendance_" + branch.getSelectedItem();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            count = 0;
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            String columnName = metaData.getColumnName(columnCount);
            if (!rs.isBeforeFirst()) {
                aorp.setEnabled(false);
                save.setEnabled(false);
                JOptionPane.showMessageDialog(null, "There are no students in the selected Branch...");
                branchError.setText("Select your Branch");
            } else {
                if (!columnName.equals(todaydate)) {
                    createColumn(branch.getSelectedItem() + "");
                } else {
                    columnExists = true;
                }
            }
            while (rs.next()) {
                int no = rs.getInt("sno");
                String n = rs.getString("name");
                String regdno = rs.getString("regd");
                String na = capitalizeWords(n);
                insertIntoList(no + "", na, regdno);
                if (columnExists) {
                    String ap = rs.getString(columnName);
                    oldAttendance.add(ap);
                }
                count++;
            }
            for (int i = 0; i < count; i++) {

                JLabel ds, dl, dl1, dl2;
                ds = new JLabel("--------------");
                ds.setFont(new java.awt.Font("Segoe UI", 0, 12));

                dl = new JLabel("------------------------------------------------------------------------------------");
                dl.setFont(new java.awt.Font("Segoe UI", 0, 12));

                dl1 = new JLabel("----------------------------------------------");
                dl1.setFont(new java.awt.Font("Segoe UI", 0, 12));

                dl2 = new JLabel("------------------------------------");
                dl2.setFont(new java.awt.Font("Segoe UI", 0, 12));

                sno.add(snosList.get(i));
                sno.add(ds);

                name.add(namsList.get(i));
                name.add(dl);

                regd.add(regdsList.get(i));
                regd.add(dl1);

                abp.add(checkboxes.get(i));
                abp.add(dl2);
                if (columnExists) {
                    if (oldAttendance.get(i) == null || oldAttendance.get(i).equals("P")) {
                        checkboxes.get(i).setSelected(false);
                    }else{
                        checkboxes.get(i).setSelected(true);
                    }
                }
            }
            attendance.revalidate();
            attendance.repaint();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    public int totalDays, daysPresent, attendancePercent;

    public void getDaysAttendance(String regdNo) {
        try {
            String query = "select dayspresent,totaldays,attendance from attendance_" + branch.getSelectedItem() + " where regd=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, regdNo);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                daysPresent = Integer.parseInt(rs.getString(1));
                totalDays = Integer.parseInt(rs.getString(2));
                attendancePercent = Integer.parseInt(rs.getString(3));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void calculateAttendance() {
        getNewAttendance();
        getOldAttendance();
        for (int i = 0; i < count; i++) {
            String regdNo = regdsList.get(i).getText();
            String oldAtten = oldAttendance.get(i);
            String newAtten = newAttendance.get(i);
            if (oldAtten == null) {
                getDaysAttendance(regdNo);
                totalDays++;
                if (newAtten.equals("P")) {
                    daysPresent++;
                    insertAttendance(regdNo, newAtten);
                } else {
                    insertAttendance(regdNo, "A");
                }
                double a = (double) daysPresent / totalDays * 100;
                attendancePercent = (int) a;
                saveAttendance(regdNo);
            } else if (!oldAttendance.get(i).equals(newAttendance.get(i))) {
                regdNo = regdsList.get(i).getText();
                getDaysAttendance(regdNo);

                if (oldAtten.equals("P")) {
                    daysPresent--;
                    insertAttendance(regdNo, "A");
                } else {
                    daysPresent++;
                    insertAttendance(regdNo, "P");
                }
                double a = (double) daysPresent / totalDays * 100;
                attendancePercent = (int) a;
                saveAttendance(regdNo);
            }

        }
    }

    public void getOldAttendance() {
        oldAttendance.clear();
        try {
            String query = "select `" + todaydate + "` from attendance_" + branch.getSelectedItem();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                oldAttendance.add(rs.getString(todaydate));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    public void getNewAttendance() {
        newAttendance.clear();
        for (int i = 0; i < count; i++) {
            if (checkboxes.get(i).isSelected()) {
                newAttendance.add("A");
            } else {
                newAttendance.add("P");
            }
        }
    }

    public void insertAttendance(String regdNo, String newAtt) {
        try {
            String query = "update attendance_" + branch.getSelectedItem() + " set `" + todaydate + "`=? where regd=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, newAtt);
            pst.setString(2, regdNo);
            pst.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void saveAttendance(String regdNo) {
        try {
            String query = "update attendance_" + branch.getSelectedItem() + " set totaldays=?,dayspresent=?,attendance=? where regd=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, totalDays + "");
            pst.setString(2, daysPresent + "");
            pst.setString(3, attendancePercent + "");
            pst.setString(4, regdNo);
            pst.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void insertIntoList(String s, String n, String r) {
        label = new JLabel(s);
        label.setFont(new java.awt.Font("Segoe UI", 0, 18));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        label1 = new JLabel(n);
        label1.setFont(new java.awt.Font("Segoe UI", 0, 18));
        
        label2 = new JLabel(r);
        label2.setFont(new java.awt.Font("Segoe UI", 0, 18));
        label2.setHorizontalAlignment(SwingConstants.CENTER);

        JCheckBox cob = new JCheckBox();
        cob.setFont(new java.awt.Font("Segoe UI", 0, 18));
        cob.setHorizontalAlignment(SwingConstants.CENTER);

        checkboxes.add(cob);
        snosList.add(label);
        namsList.add(label1);
        regdsList.add(label2);

    }

    public void createColumn(String branch) {
        try {
            totalDays++;
            String query = "alter table attendance_" + branch + " add `" + todaydate + "` varchar(4)";
            PreparedStatement create = conn.prepareStatement(query);
            create.executeUpdate();
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate column name")) {
            } else {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    String abranch;
    DefaultTableModel amodel;

    public void attendanceTable() {
        TableModel tablemodel = A_table.getModel();
        if (tablemodel instanceof DefaultTableModel defaultTableModel) {
            amodel = defaultTableModel;
        }
        if (amodel.getRowCount() != 0) {
            amodel.setRowCount(0);
            A_table.repaint();
        }
        try {
            abranch = "" + A_branch.getSelectedItem();
            String Querry = "select * from attendance_" + abranch;
            Statement pst1 = conn.createStatement();
            ResultSet rs = pst1.executeQuery(Querry);
            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(null, "There are no students");
                branchErrorView.setText("Select your Branch");
            } else {

            }
            while (rs.next()) {
                String tsno = rs.getString("sno");
                String tregd = rs.getString("regd");
                String tname = rs.getString("name");
                String tdayspresent = rs.getString("dayspresent");
                String ttotaldays = rs.getString("totaldays");
                String tattendance = rs.getString("attendance") + "%";

                String[] obj = {tsno, tregd, tname, tdayspresent, ttotaldays, tattendance};
                amodel.addRow(obj);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        amritaSaiImage = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        branch = new javax.swing.JComboBox<>();
        jlabel = new javax.swing.JLabel();
        aorp = new javax.swing.JComboBox<>();
        save = new javax.swing.JButton();
        date = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        attendance = new javax.swing.JPanel();
        sno = new javax.swing.JPanel();
        name = new javax.swing.JPanel();
        regd = new javax.swing.JPanel();
        abp = new javax.swing.JPanel();
        branchError = new javax.swing.JLabel();
        back = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        A_table = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        A_branch = new javax.swing.JComboBox<>();
        A_print = new javax.swing.JButton();
        branchErrorView = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Attendance");
        setBounds(new java.awt.Rectangle(10, 0, 0, 0));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(50, 34, 116));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel5.add(amritaSaiImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 100, 440, 276));

        jPanel2.setBackground(new java.awt.Color(50, 34, 116));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Branch");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 102, 40));

        branch.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        branch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CSE", "CSM", "CSD", "CBA", "CIC", "EEE", "ECE", "MECH", "CIVIL" }));
        branch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                branchActionPerformed(evt);
            }
        });
        jPanel2.add(branch, new org.netbeans.lib.awtextra.AbsoluteConstraints(112, 2, 130, 40));

        jlabel.setBackground(new java.awt.Color(255, 255, 255));
        jlabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jlabel.setForeground(new java.awt.Color(255, 255, 255));
        jlabel.setText("Date");
        jPanel2.add(jlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(305, 0, 73, 40));

        aorp.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        aorp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "P", "A" }));
        aorp.setEnabled(false);
        aorp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aorpActionPerformed(evt);
            }
        });
        jPanel2.add(aorp, new org.netbeans.lib.awtextra.AbsoluteConstraints(564, 2, 130, 40));

        save.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        save.setText("save");
        save.setEnabled(false);
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });
        jPanel2.add(save, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 1, 130, 40));

        date.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        date.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(date, new org.netbeans.lib.awtextra.AbsoluteConstraints(384, 0, 130, 40));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("S NO");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 48, 100, 35));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Name");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(112, 48, 266, 35));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Registration No");
        jLabel5.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(384, 48, 260, 35));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Absent/Present");
        jLabel6.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 48, 214, 35));

        jPanel5.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(499, 6, 870, 80));

        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.setHorizontalScrollBar(null);

        sno.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        name.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(0, 0, 0)));
        name.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        regd.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(0, 0, 0)));
        regd.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        abp.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(0, 0, 0)));
        abp.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        javax.swing.GroupLayout attendanceLayout = new javax.swing.GroupLayout(attendance);
        attendance.setLayout(attendanceLayout);
        attendanceLayout.setHorizontalGroup(
            attendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(attendanceLayout.createSequentialGroup()
                .addComponent(sno, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(regd, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(abp, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        attendanceLayout.setVerticalGroup(
            attendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(regd, javax.swing.GroupLayout.DEFAULT_SIZE, 638, Short.MAX_VALUE)
            .addComponent(abp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(attendance);

        jPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(484, 92, 890, 640));

        branchError.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        branchError.setForeground(new java.awt.Color(255, 255, 255));
        branchError.setText("Select your Branch");
        jPanel5.add(branchError, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 200, 40));

        back.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        back.setText("back");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });
        jPanel5.add(back, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 25, 100, 40));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Upload", jPanel1);

        jPanel3.setBackground(new java.awt.Color(0, 0, 102));
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        A_table.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        A_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sno", "Regd No", "Name", "Days Attended", "Working Days", "Attendance"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        A_table.setCellSelectionEnabled(true);
        A_table.setGridColor(new java.awt.Color(50, 34, 116));
        A_table.setRowHeight(30);
        A_table.setSelectionBackground(new java.awt.Color(50, 34, 116));
        A_table.setSelectionForeground(new java.awt.Color(255, 255, 255));
        A_table.setShowGrid(true);
        jScrollPane2.setViewportView(A_table);
        A_table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (A_table.getColumnModel().getColumnCount() > 0) {
            A_table.getColumnModel().getColumn(0).setPreferredWidth(16);
            A_table.getColumnModel().getColumn(2).setPreferredWidth(234);
        }

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(234, 92, 923, 581));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Branch");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(234, 51, 100, 35));

        A_branch.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        A_branch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CSE", "CSM", "CSD", "CBA", "CIC", "EEE", "ECE", "MECH", "CIVIL" }));
        A_branch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_branchActionPerformed(evt);
            }
        });
        jPanel3.add(A_branch, new org.netbeans.lib.awtextra.AbsoluteConstraints(371, 51, 100, 35));

        A_print.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        A_print.setForeground(new java.awt.Color(50, 34, 116));
        A_print.setText("Print");
        A_print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A_printActionPerformed(evt);
            }
        });
        jPanel3.add(A_print, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 50, 130, 35));

        branchErrorView.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        branchErrorView.setForeground(new java.awt.Color(255, 255, 255));
        branchErrorView.setText("Select your Branch");
        jPanel3.add(branchErrorView, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 50, 200, 40));

        jTabbedPane1.addTab("View", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1442, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void aorpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aorpActionPerformed
        String ab = "" + aorp.getSelectedItem();
        if (ab.equals("A")) {
            for (JCheckBox cbx : checkboxes) {
                cbx.setSelected(true);
            }
        } else {
            for (JCheckBox cbx : checkboxes) {
                cbx.setSelected(false);
            }
        }
    }//GEN-LAST:event_aorpActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        getOldAttendance();
        getNewAttendance();
        calculateAttendance();
    }//GEN-LAST:event_saveActionPerformed

    private void branchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_branchActionPerformed
        branchError.setText("");
        aorp.setEnabled(true);
        save.setEnabled(true);
        checkboxes.clear();
        snosList.clear();
        namsList.clear();
        regdsList.clear();
        oldAttendance.clear();
        newAttendance.clear();
        getData();
    }//GEN-LAST:event_branchActionPerformed

    private void A_branchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_branchActionPerformed
        branchErrorView.setText("");
        attendanceTable();
    }//GEN-LAST:event_A_branchActionPerformed

    private void A_printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A_printActionPerformed
        try {
            amodel = (DefaultTableModel) A_table.getModel();
            if (amodel.getRowCount() != 0) {
                A_table.print();
            } else {
                JOptionPane.showMessageDialog(null, "Table is empty...");
            }
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_A_printActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int result = JOptionPane.showConfirmDialog(Attendance.this, "Do you want to exit", "Closing", JOptionPane.YES_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_formWindowClosing

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        dispose();
        MainFrame mf = new MainFrame();
        mf.setVisible(true);
    }//GEN-LAST:event_backActionPerformed


    private javax.swing.JComboBox<String> A_branch;
    private javax.swing.JButton A_print;
    private javax.swing.JTable A_table;
    private javax.swing.JPanel abp;
    private javax.swing.JLabel amritaSaiImage;
    private javax.swing.JComboBox<String> aorp;
    private javax.swing.JPanel attendance;
    private javax.swing.JButton back;
    private javax.swing.JComboBox<String> branch;
    private javax.swing.JLabel branchError;
    private javax.swing.JLabel branchErrorView;
    private javax.swing.JLabel date;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel jlabel;
    private javax.swing.JPanel name;
    private javax.swing.JPanel regd;
    private javax.swing.JButton save;
    private javax.swing.JPanel sno;
    // End of variables declaration//GEN-END:variables
}
