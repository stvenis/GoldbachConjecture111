package GoldbachConjecture;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * 
 * 求哥德巴赫分拆数最多和最少的大偶数（400-500之间）算法
 * 
 */
public class GoldbachConjecture5 {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		try (Connection conn = DBUtils.getConn()) {
			String sql = "truncate  even_and_prime";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeUpdate(sql);
			
     
			
			while (true) {

				System.out.println("请输入大于2的小偶数（2退出）：");
				long n1 = sc.nextLong();
				System.out.println("请输入大于2的大偶数（2退出）：");
				long n2 = sc.nextLong();
				if (n1 < 2 || n2<2) {
					System.out.println("请输入大于2的偶数");
				} else if (n1 == 2 ||n2==2) {
					System.out.println("程序结束");
					break;
				} else if ((n1 > 2&&n1 % 2 != 0)||(n2 > 2&&n2% 2 != 0)) {
					System.out.println("您输入的数据有非偶数的");
				} else {
					long  temp=0;
                    if(n1>n2) {
                    	temp=n1;
                    	n1=n2;
                    	n2=temp;
                    }
					for (long i = n2; i >=n1; i = i - 2) {
						GoldbachConjecture(i);
					}
				}

			}
			/**
			 * select even,prime1,prime2 from even_and_prime where even in (select even from
			 * (select even,count(1) as cnt from even_and_prime group by even order by
			 * count(1) desc limit 0,1) as even_temp)
			 */
			//String sql1 = "select even,count(1) from even_and_prime  group by even  order  by count(1)  desc  limit  0,5";
			String sql1 ="select even,count(1) from \r\n" + 
					"even_and_prime \r\n" + 
					"group by \r\n" + 
					"even \r\n" + 
					"having  count(1)=(select count(1) from \r\n" + 
					"even_and_prime \r\n" + 
					"where even in \r\n" + 
					"(select even from\r\n" + 
					"(select even,count(1)  \r\n" + 
					"from \r\n" + 
					"even_and_prime \r\n" + 
					"group by \r\n" + 
					"even \r\n" + 
					"order by count(1) desc\r\n" + 
					"limit 0,1) as even_temp))";
			
			String sql2 ="select even,count(1) from \r\n" + 
					"even_and_prime \r\n" + 
					"group by \r\n" + 
					"even \r\n" + 
					"having  count(1)=(select count(1) from \r\n" + 
					"even_and_prime \r\n" + 
					"where even in \r\n" + 
					"(select even from\r\n" + 
					"(select even,count(1)  \r\n" + 
					"from \r\n" + 
					"even_and_prime \r\n" + 
					"group by \r\n" + 
					"even \r\n" + 
					"order by count(1) \r\n" + 
					"limit 0,1) as even_temp))";
			PreparedStatement ps1 = conn.prepareStatement(sql1);
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			ResultSet  rs1 = ps1.executeQuery();
			ResultSet  rs2 = ps2.executeQuery();
			System.out.println("您所查询的哥德巴赫分拆数最多的偶数和它们的对数的结果如下所示：");
			while (rs1.next()) {
				String even = rs1.getString("even");
				String count1 = rs1.getString("count(1)");
				System.out.println(even + "==========>" + count1);
				
				

			}
			System.out.println("-------------------------------------------------");
			System.out.println("您所查询的哥德巴赫分拆数最少的偶数和它们的对数的结果如下所示：");
			while (rs2.next()) {
				String even = rs2.getString("even");
				String count1 = rs2.getString("count(1)");
				System.out.println(even + "==========>" + count1);
				
				

			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		sc.close();
	}

	// GoldbachConjecture(n);

	// System.out.println(isPrime(10L));

	/**
	 * 验证任一大于2的偶数都可写成两个质数之和
	 * 
	 * @param num
	 */
	private static void GoldbachConjecture(long num) {
		// 首先将num分解为两个奇数之和
		long num1, num2;
		// 得到连接
		try (Connection conn = DBUtils.getConn()) {

			String sql = "insert into  even_and_prime  (even,prime1,prime2)  values (?,?,?)";
			// 创建预编译的SQL执行对象
			PreparedStatement ps = conn.prepareStatement(sql);
			for (num1 = 1; num1 <= num / 2; num1++) {
				num2 = num - num1;
				// 分别判断组合的两个数字是否均为质数
				if (isPrime(num1) && isPrime(num2)) {
					System.out.println(num + "=" + num1 + "+" + num2);
					// 把？换成真正的变量
					ps.setLong(1, num);
					ps.setLong(2, num1);
					ps.setLong(3, num2);
					// 执行
					ps.executeUpdate();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 验证是否是质数
	 * 
	 * @param n 要判断的数字
	 * @return true or false
	 */
	private static boolean isPrime(long n) {
		// 1和2单独判断，1已经不是素数，2是最小素数
		if (n == 1) {
			return false;
		} else if (n == 2) {
			return true;
		} else {
			for (long i = 2; i <= Math.sqrt(n); i++) {
				if (n % i == 0) {
					return false;
				}

			}
			return true;
		}

	}
}
