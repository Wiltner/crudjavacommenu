import java.sql.*;
import java.util.Scanner;

public class Main {

    final String URL = "jdbc:mysql://localhost:3306/cadaluno1";
    final String USER = "root";
    final String PASSWORD = "root99";
    Scanner sc = new Scanner(System.in);

    public Main(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conectado!");

            while(true){
                System.out.println("Menu:");
                System.out.println("1 - Cadastrar novo aluno");
                System.out.println("2 - Editar cadastro");
                System.out.println("3 - Visualizar relatório de alunos");
                System.out.println("4 - Excluir cadastro");
                System.out.println("0 - Sair");

                int escolha = sc.nextInt();
                sc.nextLine();

                switch (escolha){
                    case 1:
                        cadastrarAluno(con);
                        break;
                    case 2:
                        editarCadastro(con);
                        break;
                    case 3:
                        visualizarRelatorio(con);
                        break;
                    case 4:
                        excluirCadastro(con);
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        con.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente!");
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    private void cadastrarAluno(Connection con) throws SQLException{
        String insert = "INSERT INTO aluno (nome, matricula) VALUES (?, ?)";

        System.out.println("Informe o nome do aluno");
        String nomeAluno = sc.nextLine();
        System.out.println("Informe a matricula do aluno");
        int matriculaAluno = sc.nextInt();

        PreparedStatement stmt = con.prepareStatement(insert);
        stmt.setString(1, nomeAluno);
        stmt.setInt(2, matriculaAluno);

        int linhasAfetadas = stmt.executeUpdate();
        System.out.println("Dados inseridos!");
    }

    private void editarCadastro(Connection con) throws SQLException{
        String consulta = "SELECT * FROM aluno WHERE matricula = ?";

        PreparedStatement consultaStmt = con.prepareStatement(consulta);

        System.out.println("Informe o número da matrícula que deseja editar:");
        int matriculaParaEditar = sc.nextInt();
        sc.nextLine();

        consultaStmt.setInt(1, matriculaParaEditar);
        ResultSet rs = consultaStmt.executeQuery();

        if(rs.next()){
            System.out.println("Digite o novo nome do aluno");
            String novoNome = sc.nextLine();
            System.out.println("Digite a nova matricula do aluno");
            int novaMatricula = sc.nextInt();

            String update = "UPDATE aluno SET nome = ?, matricula = ? WHERE matricula = ?";
            PreparedStatement updateStmt = con.prepareStatement(update);
            updateStmt.setString(1, novoNome);
            updateStmt.setInt(2, novaMatricula);
            updateStmt.setInt(3, matriculaParaEditar);
            updateStmt.executeUpdate();

            System.out.println("Dados atualizados!");
        }else{
            System.out.println("Aluno não encontrado!");
        }
    }

    private void visualizarRelatorio(Connection con) throws SQLException{
        String consulta = "SELECT * FROM aluno";

        PreparedStatement consultaStmt = con.prepareStatement(consulta);

        ResultSet rs = consultaStmt.executeQuery();

        while(rs.next()){
            System.out.println("Nome: " + rs.getString("nome"));
            System.out.println("Matrícula: " + rs.getInt("matricula"));
        }
    }

    private void excluirCadastro(Connection con) throws SQLException{
        String delete = "DELETE FROM aluno WHERE  matricula = ?";
        PreparedStatement stmt = con.prepareStatement(delete);

        System.out.println("Informe a cadastro que deseja excluir:");
        int matriculaParaExcluir = sc.nextInt();

        stmt.setInt(1, matriculaParaExcluir);
        int linhasAfetadas = stmt.executeUpdate();

        if(linhasAfetadas>0){
            System.out.println("Aluno excluido!");
        }else{
            System.out.println("Aluno não encontrado!");
        }
    }



    public static void main(String[] args) {


        Main main = new Main();
    }
}
