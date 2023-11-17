import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.janelas.Janela;
import com.github.britooo.looca.api.group.janelas.JanelaGrupo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import oshi.SystemInfo;

import java.util.List;
import java.util.Scanner;

public class TesteLooca {
    public static void main(String[] args) {
        Looca looca = new Looca();

        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        JanelaGrupo windows = new JanelaGrupo(new SystemInfo());
        List<Janela> janelas = windows.getJanelas();

        Scanner leitor = new Scanner(System.in);

        Scanner email = new Scanner(System.in);
        Scanner senha = new Scanner(System.in);

        System.out.println("Digite seu email: ");
        String emailUser = email.next();
        System.out.println("Digite sua senha: ");
        String senhaUser = senha.next();

//        Usuario user01 = new Usuario(emailUser, senhaUser);

        List<Usuario> usuariosBanco = con.query("SELECT email, senha FROM usuario " +
                        "WHERE email = '%s' AND senha = '%s'".formatted(emailUser, senhaUser),
                new BeanPropertyRowMapper<>(Usuario.class));

        if (usuariosBanco.size() < 1) {
            System.out.println("Email e/ou senha inválidos");
        } else {
            Integer escolha;
            do {
                String painel = """
                        |----------------------------------------------|
                              Monitoramento de Janelas da NOCT.U          
                        |----------------------------------------------|
                        | 1 - Visualizar Janelas identificadas         |
                        | 2 - Visualizar janelas em grupos de processos|
                        | 3 - Visualizar todas as janelas operantes    |
                        | 4 - Visualizar quantidade de janelas visiveis|
                        | 5 - Visualizar quantidade total de janelas   |
                        | 0 - Sair                                     |
                        |----------------------------------------------|
                                            
                        """;
                System.out.println(painel);

                escolha = leitor.nextInt();

                switch (escolha) {
                    case 1 -> {
                        for (Janela j : janelas) {
                            if (j.getTitulo() != "") {
                                System.out.println("ID da janela: " + j.getJanelaId() + " - " + "Título da janela: " + j.getTitulo());

                            }
                        }
                    }
                    case 2 -> {
                        System.out.println(windows.getJanelas());
                    }
                    case 3 -> {
                        for (Janela j : janelas) {
                            System.out.println(j);
                        }
                    }
                    case 4 -> {
                        System.out.println("Total das janelas visíveis: " + windows.getTotalJanelasVisiveis());
                    }
                    case 5 -> {
                        System.out.println("Total das janelas: " + windows.getTotalJanelas());
                    }

                    case 0 -> {
                        System.out.println("Encerrando...");
                    }

                    default -> {
                        System.out.println("valor inválido");
                    }
                }


            } while (escolha != 0);


        }
    }

}
