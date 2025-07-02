import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.*;

class Book{
    static int  counter =0;
    int id;
    String title;
    String author;
    String genre ;
   boolean isAvailable;

    public Book(String title, String author, String genre) {
        this.id = counter++;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isAvailable = true;
    }
}
class Member{
    static int counter=0;
    int id;
    String username;
    ArrayList<Book> borrowedbook;

   public Member(String username){
       this.id = counter++;
       this.username = username;
       this.borrowedbook = new ArrayList<>();
   }
   boolean borrowBooks(Book book){
       if(this.borrowedbook.size() < 5 && book.isAvailable){
           this.borrowedbook.add(book);
           book.isAvailable = false;
           return true;
       }
       return false;

   }
     boolean returnBook(Book book) {
       if(this.borrowedbook.remove(book)){
           book.isAvailable = true;
           return true;
       }
       return false;
    }
}
public class Main {
    static Scanner sc = new Scanner(System.in);
    static List<Book> books = new ArrayList<>();
    static  List<Member> members = new ArrayList<>();
    static Member currentMember;

    static final String admin = "admin";
     static final String password ="password";
    public static void main(String[] args) {
    Connection conn = DBconnection.getConnection();
     if(conn!=null){
         System.out.println("DB connected Succesfull");
     }
     else{
         System.out.println("Connection Faild");
     }

        while(true){
       System.out.println("Enter role(user/admin)");
       String role = sc.nextLine();

       if(role.equals("admin")){
           loginAsAdmin();
       }
       else if(role.equals("user")){
           loginAsUser();
        }
       else{
           System.out.println("Invalid Input");
       }
    }

    }
    private static void loginAsAdmin(){
        System.out.println("Enter your username");
        String user = sc.nextLine();
        System.out.println("Enter your password");
        String pass = sc.nextLine();

        if(user.equals(admin) && pass.equals(password)){
            System.out.println("Loggined As Admin");
            adminMenu();
        }
        else{
            System.out.println("Wrong Crenddetial");
        }
    }
    private static void loginAsUser(){
        System.out.println("Enter your username");
        String user = sc.nextLine();
        currentMember = findUserOrCreate(user);
        userMenu();
    }
    private static Member findUserOrCreate(String user){
        for(Member meb : members){
            if(meb.username.equals(user)){
                return meb;
            }
        }
        Member newmember = new Member(user);
        addMemberTodb(user);
        members.add(newmember);
        return newmember;
    }
    private static void userMenu(){
        while (true){
            System.out.println("\nLibrary Management System");
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. Display All Books");
            System.out.println("4. Display All Members");
            System.out.println("5. Exit");
            int input = sc.nextInt();
            sc.nextLine();
            switch (input){
                case 1:borrowBook();break;
                case 2:returnBook();break;
                case 3:displayBooks();break;
                case 4:displayMembers();break;
                case 5: return;
                default:
                    System.out.println("Invalid input ");
            }
        }

    }

    private static void returnBook() {
        displayBooks();
        System.out.println("Enter Book ID to Update");
        int  id = sc.nextInt();
        sc.nextLine();
        Book book = findBookbyid(id);
        if(book==null){
            System.out.println("Sorry book not found");
            return;
        }
        boolean check= currentMember.returnBook(book);
        if(check )
            System.out.println("Book returned successfully");
        else
            System.out.println("Sorry Can't find the book");
    }

    private static void borrowBook() {
        displayBooks();
        System.out.println("Enter Book ID ");
        int  id = sc.nextInt();
        sc.nextLine();
        Book book = findBookbyid(id);
        if(book==null){
            System.out.println("Sorry book not found");
            return;
        }

        boolean check = currentMember.borrowBooks(book);
        if(check)
            System.out.println("Book Borrowed SUccesfully");
        else
            System.out.println("Sorry you Have't borrowed the book");
    }

    private static void adminMenu(){
        while(true){
            System.out.println("\nLibrary Management System");
            System.out.println("1. Add Book");
            System.out.println("2. Update Book");
            System.out.println("3. Remove Book");
            System.out.println("4. Add Member");
            System.out.println("5. Display All Books");
            System.out.println("6. Display All Members");
            System.out.println("7. Exit");
            int input = sc.nextInt();
            sc.nextLine();
            switch (input){
                case 1: addBook();break;
                case 2: updateBook();break;
                case 3: removeBook();break;
                case 4: addMember();break;
                case 5: displayBooks();break;
                case 6: displayMembers();break;
                case 7: return;
                default:
                    System.out.println("Invalid Input please try Again");
        }

        }
    }
//------------------------pending-----------------------------
    private static void displayMembers() {
        if(members.isEmpty()){
            System.out.println("No such Members");
        }
        System.out.println("List of Members");
        for(Member meb :members){
            System.out.println("UserName:"+meb.username);
            for(Book list: meb.borrowedbook){
                System.out.println("borrowed Books\n");
                System.out.println("Book-Name : "+list.title);
                System.out.println("Book-author : "+list.author);
                System.out.println("Book-genere: "+list.genre);
            }
        }
    }

    private static void displayBooks() {
        if(books.isEmpty()){
            System.out.println("Books is empty");
        }
        System.out.println("List of Books \n");
        for(Book book : books){
            if(book.isAvailable){
                System.out.println("Id: "+book.id+" Title: "+book.title+" Authour: "+book.author +" Genere : "+book.genre+" Books Available : "+book.isAvailable);
            }
        }
    }

    private static void addMember() {
        System.out.println("Enter the username To add");
        String user = sc.nextLine();
        members.add(new Member(user));
        System.out.println("Members added succesfully");
    }

    private static void removeBook() {
        displayBooks();
        System.out.println("Enter Book ID to remove");
        int  id = sc.nextInt();
        sc.nextLine();
        Book book = findBookbyid(id);

        books.remove(book);
        System.out.println("Book removed Succesfully");
    }

//    private static Book findbook(String title, String author) {
//        for(Book book : books){
//            if(book.title.equals(title) && book.author.equals(author)){
//                return book;
//            }
//        }
//        return null;
//    }

    private static void addBook(){
        System.out.println("Enter The Book title");
        String title = sc.nextLine();
        title.toLowerCase();
        System.out.println("Enter The Author Name");
        String author = sc.nextLine();
        author.toLowerCase();
        System.out.println("Enter the genere");
        String genere =sc.nextLine();
        genere.toLowerCase();
        books.add(new Book(title,author,genere));
        addBookTodb(title,author,genere,true);
        System.out.println("Book Added Sucessfully");
    }
    private static void updateBook() {
        displayBooks();
        System.out.println("Enter Book ID to Update");
        int  id = sc.nextInt();
        sc.nextLine();
        Book updateBook = findBookbyid(id);

        if(updateBook!=null){
            System.out.println("Update Menu ");
            System.out.println("1. New Title");
            System.out.println("2. New Autor");
            System.out.println("3. New genere");
            int input = sc.nextInt();
            sc.nextLine();
            switch (input){
                case 1:
                    System.out.println("Enter the title to update");
                    String updateTitle = sc.nextLine();
                    updateBook.title = updateTitle;
                    break;
                case 2:
                    System.out.println("Enter the Author to update");
                    String updateauthor = sc.nextLine();
                    updateBook.author = updateauthor;
                    break;
                case 3:
                    System.out.println("Enter genere To update");
                    String updategenere = sc.nextLine();
                    updateBook.genre= updategenere;
                    break;
                default:
                    System.out.println("Enter The valid Option");    
            }
        }
    }
    static Book findBookbyid(int id){
        for(Book book : books ){
            if(book.id == (id)){
                return book;
            }
        }
        return null;
    }
//-----------------------------DB-connections-----------------------------------------


}