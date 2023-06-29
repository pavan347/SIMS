
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author pavan kumar
 */
public class View extends javax.swing.JFrame {

    /**
     * Creates new form View
     */
    static Connection conn;

    public View() throws SQLException {
        try {
            initComponents();
            ImageIcon icon = new ImageIcon("C:\\Program Files\\SIMS\\StudentWhite.png");
            setIconImage(icon.getImage());
            setTitle("View");

            DataBaseConnection dbc = new DataBaseConnection();
            conn = dbc.getCon();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    DefaultTableModel model;
    String tablecourse;

    public void tableDetails() {
        TableModel tablemodel = Table_details.getModel();
        if (tablemodel instanceof DefaultTableModel defaultTableModel) {
            model = defaultTableModel;
        }
        model.setRowCount(0);
        Table_details.repaint();
        try {
            tablecourse = "" + table_course.getSelectedItem();
            String Querry = "select * from " + tablecourse;
            Statement pst1 = conn.createStatement();
            ResultSet rs = pst1.executeQuery(Querry);
            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(null, "There are no students in the selected Branch...");
                branchError.setText("Select your Branch");
            }
            while (rs.next()) {
                String tsno = rs.getString("sno");
                String tregd = rs.getString("regd");
                String tname = rs.getString("name");
                String tfname = rs.getString("fathername");
                String tmname = rs.getString("mothername");
                String tdob = rs.getString("dob");
                String tage = rs.getString("age");
                String taddress = rs.getString("address");
                String tphoneno = rs.getString("phoneno");
                String temailid = rs.getString("emailid");
                String taadhar = rs.getString("aadhar");
                String tgender = rs.getString("gender");
                String tcategory = rs.getString("category");
                String tpassword = rs.getString("password");
                String[] obj = {tsno, tregd, tname, tfname, tmname, tgender, tdob, tage, tphoneno, taddress, temailid, taadhar, tcategory, tpassword};
                model = (DefaultTableModel) Table_details.getModel();
                model.addRow(obj);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     *
     * @param str
     */
    public void search(String str) {
        try {
            model = (DefaultTableModel) Table_details.getModel();
            TableRowSorter<?> trs = new TableRowSorter<>(model);
            Table_details.setRowSorter(trs);
            trs.setRowFilter(RowFilter.regexFilter(str));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "illegal character...");
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

        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        table_course = new javax.swing.JComboBox<>();
        Table_search = new javax.swing.JTextField();
        tfulldetails = new javax.swing.JButton();
        branchError = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table_details = new javax.swing.JTable();
        back = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBounds(new java.awt.Rectangle(10, 0, 0, 0));
        setPreferredSize(new java.awt.Dimension(1510, 820));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(50, 34, 116));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(50, 34, 116));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Course");
        jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 84, 100, 35));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Search");
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 30, 100, 35));

        table_course.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        table_course.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CSE", "CSD", "CIC", "CSM", "CBA", "ECE", "EEE", "CIVIL", "MECH" }));
        table_course.setSelectedIndex(-1);
        table_course.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                table_courseActionPerformed(evt);
            }
        });
        jPanel4.add(table_course, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 86, 107, 35));

        Table_search.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Table_search.setSelectionColor(new java.awt.Color(255, 255, 255));
        Table_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Table_searchActionPerformed(evt);
            }
        });
        Table_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Table_searchKeyReleased(evt);
            }
        });
        jPanel4.add(Table_search, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 31, 234, 35));

        tfulldetails.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        tfulldetails.setText("Full Details");
        tfulldetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfulldetailsActionPerformed(evt);
            }
        });
        jPanel4.add(tfulldetails, new org.netbeans.lib.awtextra.AbsoluteConstraints(885, 85, 146, 33));

        branchError.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        branchError.setForeground(new java.awt.Color(255, 255, 255));
        branchError.setText("Select your Branch");
        jPanel4.add(branchError, new org.netbeans.lib.awtextra.AbsoluteConstraints(315, 84, 200, 35));

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, -1, -1));

        Table_details.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Table_details.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Table_details.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "sno", "Regd", "Name", "Father Name", "Mother Name", "Gender", "DOB", "Age", "Phone no", "Address", "Email ID", "Aadhar", "Category", "Password"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Table_details.setCellSelectionEnabled(true);
        Table_details.setEditingColumn(0);
        Table_details.setEditingRow(0);
        Table_details.setGridColor(new java.awt.Color(50, 34, 116));
        Table_details.setRowHeight(30);
        Table_details.setSelectionBackground(new java.awt.Color(50, 34, 116));
        Table_details.setShowGrid(true);
        jScrollPane1.setViewportView(Table_details);
        Table_details.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        if (Table_details.getColumnModel().getColumnCount() > 0) {
            Table_details.getColumnModel().getColumn(0).setMinWidth(7);
            Table_details.getColumnModel().getColumn(0).setPreferredWidth(7);
            Table_details.getColumnModel().getColumn(1).setPreferredWidth(60);
            Table_details.getColumnModel().getColumn(5).setPreferredWidth(30);
            Table_details.getColumnModel().getColumn(6).setPreferredWidth(60);
            Table_details.getColumnModel().getColumn(7).setPreferredWidth(7);
            Table_details.getColumnModel().getColumn(12).setPreferredWidth(25);
            Table_details.getColumnModel().getColumn(13).setPreferredWidth(30);
        }

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 1461, 550));

        back.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        back.setText("back");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });
        jPanel3.add(back, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1515, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 1515, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 809, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 809, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void table_courseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_table_courseActionPerformed
        branchError.setText("");
        tableDetails();
    }//GEN-LAST:event_table_courseActionPerformed

    private void Table_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Table_searchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Table_searchActionPerformed

    private void Table_searchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_searchKeyReleased
        search(Table_search.getText());
    }//GEN-LAST:event_Table_searchKeyReleased

    private void tfulldetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfulldetailsActionPerformed
        int index = Table_details.getSelectedRow();
        int modifiedIndex = Table_details.convertRowIndexToModel(index);
        if (index >= 0) {
            try {
                TableModel tablemodel = Table_details.getModel();
                String regdnum = tablemodel.getValueAt(modifiedIndex, 1).toString();;
                Update u = new Update();
                u.displayDetails(regdnum, "" + table_course.getSelectedItem(), false, false);
                u.setVisibl();
                u.setVisible(true);
                dispose();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Select any row in the Table...");
        }
    }//GEN-LAST:event_tfulldetailsActionPerformed

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        dispose();
        MainFrame mf = new MainFrame();
        mf.setVisible(true);
    }//GEN-LAST:event_backActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int result = JOptionPane.showConfirmDialog(View.this, "Do you want to exit", "Closing", JOptionPane.YES_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table_details;
    private javax.swing.JTextField Table_search;
    private javax.swing.JButton back;
    private javax.swing.JLabel branchError;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> table_course;
    private javax.swing.JButton tfulldetails;
    // End of variables declaration//GEN-END:variables
}
