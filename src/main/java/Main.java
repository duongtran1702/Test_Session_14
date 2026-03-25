import java.sql.*;
import java.util.Scanner;

public class Main {
    @SuppressWarnings("CallToPrintStackTrace")
    public static boolean checkExisted(Connection conn, String id, int balance) {
        String sql = "SELECT 1 FROM accounts WHERE AccountId = ? AND balance >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setInt(2, balance);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isValidTransfer(int amountFrom, int amountTo) {
        if (amountFrom >= 0) {
            System.out.println("So tien chuyen phai < 0");
            return false;
        }

        if (amountTo <= 0) {
            System.out.println("So tien nhan phai > 0");
            return false;
        }

        if (Math.abs(amountFrom) != amountTo) {
            System.out.println("So tien giao dich khong tuong xung");
            return false;
        }
        return true;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter ID 1: ");
        String id1 = scanner.nextLine();
        System.out.print("Enter Amount 1: ");
        int balance1 = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter ID 2: ");
        String id2 = scanner.nextLine();
        System.out.print("Enter Amount 2: ");
        int balance2 = scanner.nextInt();

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            //Start Transaction
            conn.setAutoCommit(false);

            // validate logic chuyển tiền
            if (!isValidTransfer(balance1, balance2)) {
                conn.rollback();
                return;
            }

            try (CallableStatement cs1 = conn.prepareCall("{Call sp_UpdateBalance (?,?)}");
                 CallableStatement cs2 = conn.prepareCall("{Call sp_UpdateBalance(?,?)}")) {

                // kiem tra ton tai
                if (!checkExisted(conn, id1, balance1) ||
                        !checkExisted(conn, id2, 0)) {
                    System.out.println("Tai khoan k ton tai hoac so du khong du");
                    conn.rollback();
                    return;
                }

                // thuc thi lan 1
                cs1.setString(1, id1);
                cs1.setInt(2, balance1);
                int row1 = cs1.executeUpdate();

                // thuc thi lan 2
                cs2.setString(1, id2);
                cs2.setInt(2, balance2);
                int row2 = cs2.executeUpdate();

                // check
                if (row1 > 0 && row2 > 0) {
                    System.out.println("Giao dich thanh cong");
                    conn.commit();
                } else {
                    System.out.println("Giao dich that bai");
                    conn.rollback();
                }

            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}