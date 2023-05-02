import java.util.Objects;
import java.util.Scanner;

public class MainClass {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MessageBase messageBase = new MessageBase();
        while (scanner.hasNext()) {
            String text = scanner.nextLine();
            if (!Objects.equals(text, "END_OF_MESSAGE")) {
                messageBase.inputMessages(text);
            } else {
                break;
            }
        }

        while (scanner.hasNext()) {
            String quary = scanner.next();
            if (Objects.equals(quary, "qdate")) {
                Qdate qdate = new Qdate(scanner.next());
                messageBase.qdate(qdate);
            } else if (Objects.equals(quary, "qsend")) {
                messageBase.qsend(scanner.next());
            } else if (Objects.equals(quary, "qrecv")) {
                messageBase.qrecv(scanner.next());
            } else if (Objects.equals(quary, "qmess")) {
                messageBase.qmess(scanner.next(), scanner.next());
            }
        }
    }
}
