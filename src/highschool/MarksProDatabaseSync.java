/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package highschool;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import net.miginfocom.swing.MigLayout;

/**
 * @author FRED
 */
public class MarksProDatabaseSync extends JDialog {


    public void dataSync() {
        int conter = 0;
        Connection con = DbConnection2.connectDb();
        PreparedStatement ps;
        Connection con2 = DbConnection.connectDb();
        ResultSet rs;
        try {

            JProgressBar bar = new JProgressBar();
            bar.setIndeterminate(true);

            this.setSize(300, 100);

            this.setLayout(new MigLayout());
            this.add(bar, "grow,push");
            this.setTitle("transfering student registration details........");
            this.setAlwaysOnTop(true);
            this.setLocationRelativeTo(CurrentFrame.mainFrame());
            this.setIconImage(FrameProperties.icon());


            this.setVisible(true);


            String sqlstart = "Select * from admissions_table where current_class='" + "1" + "' or current_class='" + "2" + "' or current_class='" + "3" + "' or current_class='" + "4" + "' ";
            ps = con.prepareStatement(sqlstart);
            rs = ps.executeQuery();
            while (rs.next()) {
                this.setVisible(true);
                String currentclasscode = Globals.classCode("Form " + rs.getString("current_class"));
                String classadmittedcode = Globals.classCode("Form " + rs.getString("Class_admitted_to"));
                String currentstreamcode = Globals.streamcode(rs.getString("Current_Stream"));
                String streamadmittedTo = Globals.streamcode(rs.getString("Stream_admitted_to"));
                String termadmitted = Globals.termcode("Term " + rs.getString("Term_admitted"));
                String dateofbirth = rs.getString("Date_of_Birth");
                String dateofAdmission = rs.getString("Date_of_Admission");
                String gender = rs.getString("gender");
                String fname = rs.getString("Firstname").replace("'", "");
                String lname = rs.getString("lastname").replace("'", "");
                String middlename = rs.getString("surname").replace("'", "");
                String adm = rs.getString("Admission_Number");
                String contact = rs.getString("telephone_contact1");
                String wardname = rs.getString("Home_ward");
                String country = rs.getString("Home_Country");
                String province = rs.getString("Home_province");
                String constituency = rs.getString("Home_constituency");
                String county = rs.getString("Home_county");
                String kcpe_marks = rs.getString("kcpe_marks");
                String parentname = rs.getString("home_parish");
                String contact2 = rs.getString("telephone_contact2");
                if (termadmitted == null || termadmitted.equalsIgnoreCase("")) {
                    termadmitted = "Term 1";
                }

                ResultSet rs1;
                String exist = "Select admissionNumber from admission where admissionnumber='" + adm + "'";
                ps = con2.prepareStatement(exist);
                rs1 = ps.executeQuery();
                if (rs1.next()) {
                    //JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "Record existsts");
                } else {
                    conter++;

                    String constituencycode = "", wardcode = "", provincecode = "", countycode = "", countrycode = "", formcodea = "", formcodec = "", termcodea = "", termcodec = "", streamcodea = "", streamcodec = "";
                    String sql = "Select countrycode from countries where countryname='" + country + "'";
                    ps = con2.prepareStatement(sql);
                    rs1 = ps.executeQuery();
                    if (rs1.next()) {
                        countrycode = rs1.getString("Countrycode");

                    }
                    String sql2 = "Select provincecode from provinces where provincename='" + province + "'";
                    ps = con2.prepareStatement(sql2);
                    rs1 = ps.executeQuery();
                    if (rs1.next()) {
                        provincecode = rs1.getString("provincecode");
                    }

                    String sql3 = "Select countycode from counties where countyname='" + county + "'";
                    ps = con2.prepareStatement(sql3);
                    rs1 = ps.executeQuery();
                    if (rs1.next()) {
                        countycode = rs1.getString("Countycode");
                    }
                    String sql4 = "Select constituencycode from constituencies where constituencyname='" + constituency + "'";
                    ps = con2.prepareStatement(sql4);
                    rs1 = ps.executeQuery();
                    if (rs1.next()) {
                        constituencycode = rs1.getString("constituencycode");
                    }
                    String sql5 = "Select wardcode from ward where wardname='" + wardcode + "'";
                    ps = con2.prepareStatement(sql5);
                    rs1 = ps.executeQuery();
                    if (rs1.next()) {
                        wardcode = rs1.getString("wardcode");
                    }
                    String sql6 = "Select  classcode from classes where classname='" + classadmittedcode + "'";
                    ps = con2.prepareStatement(sql6);
                    rs1 = ps.executeQuery();
                    if (rs1.next()) {
                        formcodea = rs1.getString("classcode");
                    }
                    String sql7 = "Select  classcode from classes where classname='" + currentclasscode + "'";
                    ps = con2.prepareStatement(sql7);
                    rs1 = ps.executeQuery();
                    if (rs1.next()) {
                        formcodec = rs1.getString("classcode");
                    }
                    String sql8 = "Select* from streams where streamname='" + streamadmittedTo + "'";
                    ps = con2.prepareStatement(sql8);
                    rs1 = ps.executeQuery();
                    while (rs1.next()) {
                        streamcodea = rs1.getString("Streamcode");

                    }
                    String sql9 = "Select* from streams where streamname='" + currentstreamcode + "'";
                    ps = con2.prepareStatement(sql9);
                    rs1 = ps.executeQuery();
                    while (rs1.next()) {
                        streamcodec = rs1.getString("Streamcode");
                    }
                    String querry = "Select termcode from terms where termname='" + termadmitted + "'";
                    ps = con2.prepareStatement(querry);
                    rs1 = ps.executeQuery();
                    while (rs1.next()) {
                        termcodea = rs1.getString("Termcode");

                    }


                    String SQL = "Insert into admission values(?,?,?,'" + gender.toUpperCase() + "'"
                            + ",'" + dateofbirth + "','" + dateofAdmission + "','" + classadmittedcode + "','" + termadmitted + "','" + streamadmittedTo + "','" + currentclasscode + "','" + Globals.currentTerm() + "','" + currentstreamcode + "','" + kcpe_marks + "','" + countrycode + "','" + provincecode + "',"
                            + "'" + countycode + "','" + constituencycode + "','" + wardcode + "',?,'" + contact + "','" + contact2 + "','" + adm + "','" + "" + "','" + "" + "','" + "DAY PROGRAM" + "')";

                    ps = con2.prepareStatement(sql);
                    ps.setString(1, fname.toUpperCase());
                    ps.setString(2, lname.toUpperCase());
                    ps.setString(3, middlename.toUpperCase());
                    ps.setString(4, parentname.toUpperCase());
                    ps.execute();

                }

            }

            this.setTitle("transfering marks....");
            String querry = "Select * from marks_table where exam_name!='" + "TOTAL" + "' and current_year='" + "2019" + "'  ";
            con = DbConnection2.connectDb();
            ps = con.prepareStatement(querry);
            rs = ps.executeQuery();
            while (rs.next()) {

                String adm;
                String subjectcode;
                String termcode = Globals.termcode(rs.getString("current_term"));
                ;
                String year;
                String score;
                String outof;
                String percent;
                String convertedscore;
                String convertedscoreoutof;
                String grade;
                String point;
                String teachername;
                String classcode;
                String streamcode;
                String examname;
                String examcode;
                adm = rs.getString("admission_number");
                if (Globals.admissionVerifier(adm)) {

                    year = rs.getString("current_year");
                    subjectcode = rs.getString("subject_code");
                    score = rs.getString("exam_score");
                    outof = rs.getString("exam_out_of");
                    percent = rs.getString("exam_percentage");
                    convertedscore = rs.getString("converted_score");
                    convertedscoreoutof = rs.getString("converted_score_out_of");
                    grade = rs.getString("exam_grade");
                    point = rs.getString("exam_points");
                    teachername = rs.getString("class_teacher_initials");
                    classcode = Globals.classCode("Form " + rs.getString("current_class"));
                    streamcode = Globals.streamcode(rs.getString("stream"));
                    examname = rs.getString("exam_name");
                    examcode = ExamCodesGenerator.generatecode("Form " + rs.getString("current_class"), year, "Term " + rs.getString("Current_Term"), examname);
                    termcode = Globals.termcode("Term " + rs.getString("Current_Term"));
//    String testa="Select * from markstable where examname='"+examname+"' and academicYear='"+"2019"+"'  and classcode='"+classcode+"'  and subjectcode='"+subjectcode+"' and examcode='"+examcode+"' and termcode='"+termcode+"'";
//    ps=con2.prepareStatement(testa);
//    ResultSet rx=ps.executeQuery();
//    if(rx.next())
//    {
//        
//    }
//    else{

                    String sql = "Insert into markstable values('" + adm + "','" + subjectcode + "','" + classcode + "','" + streamcode + "','" + termcode + "','" + year + "','" + examname + "','" + examcode + "','" + score + "','" + outof + "','" + convertedscore + "','" + convertedscoreoutof + "','" + percent + "','" + grade + "','" + point + "','" + "1" + "','" + "1" + "',?)";
                    ps = con2.prepareStatement(sql);
                    ps.setString(1, teachername);
                    ps.execute();


                }


            }
            this.setTitle("Transfering student subject selection history.....");
            String sq = "Select * from students_subject_allocation_table where current_year='" + "2019" + "' ";
            ps = con.prepareStatement(sq);
            rs = ps.executeQuery();
            while (rs.next()) {
                String adm = rs.getString("admission_number");

                if (Globals.admissionVerifier(adm)) {
                    String year = rs.getString("Current_Year");

                    ps = con2.prepareStatement("Select * from subjects");
                    ResultSet rx = ps.executeQuery();
                    while (rx.next()) {
                        String scode = "S" + rx.getString("subjectCode");
                        String subcode = rx.getString("subjectCode");
                        if (rs.getString(scode).equalsIgnoreCase("1")) {
                            String testa = "Select * from studentsubjectallocation where admNumber='" + adm + "' and subjectcode='" + subcode + "' and academicYear='" + year + "'";
                            ps = con2.prepareStatement(testa);
                            ResultSet tt = ps.executeQuery();
                            if (tt.next()) {

                            } else {
                                String sql3 = "Insert into studentsubjectallocation values('" + subcode + "','" + adm + "','" + year + "')";
                                ps = con2.prepareStatement(sql3);
                                ps.execute();
                            }


                        } else {

                        }


                    }


                }


            }

//       this.setTitle("Updating KCPE marks........");
//       ps=con.prepareStatement("Select * from kcpe_table");
//       rs=ps.executeQuery();
//       while(rs.next())
//       {
//           String adm=rs.getString("Adm");
//           String marks=rs.getString("kcpe");
//           if(marks.equalsIgnoreCase("null"))
//           {
//               marks="";
//           }
//           ps=con2.prepareStatement("update admission set KcpeMarks='"+marks+"' where admissionNumber='"+adm+"'");
//           ps.execute();
//         
//       }

            this.dispose();
            JOptionPane.showMessageDialog(CurrentFrame.mainFrame(), "Databases Synchronized Successfully\n" + conter + " Record(s) Synchronised\n Marks must be reanalysed to obtain reports");


        } catch (HeadlessException | SecurityException | SQLException sq) {
            sq.printStackTrace();
        }

    }

}
