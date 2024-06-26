/*Código desenvolvido em conjunto com Wendel e Eric Matias. Em consonância a 2VA de Programação Orientada a Objetos do curso de Sistemas de Informação. Universidade Estadual de Goiás.
*/



/*Importações necessárias em Java para acessar classes pré-definidas para interfaces gráficas (javax.swing e java.awt), lidar com eventos de interface (java.awt.event), 
manipular arquivos (java.io), e gerenciar coleções de dados (java.util.ArrayList). Cada pacote e classe oferece funcionalidades específicas, como criar janelas, botões,
 tabelas, e manipular eventos e dados dentro de um programa Java.*/
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;


/*Nesse trecho do código da classe Agenda, o método montarPainelContato está organizando duas partes principais dentro de um painel chamado painelContato.
Primeiro, cria um painel (painelContatoInfo) para colocar informações detalhadas sobre um contato, como nome, endereço e telefone. Depois, esse método adiciona esse painel no centro do painel principal (painelContato).
Segundo, cria outro painel (painelContatoOpcoes) para incluir botões ou opções relacionadas ao contato, como salvar ou excluir informações. Esse painel é adicionado na parte inferior do painel principal (painelContato). */
class Agenda extends JFrame {
    private ArrayList<Pessoa> pessoas = new ArrayList<>(); //Cria um vetor de pessoas para armazenar seus dados
    private DefaultTableModel modeloTabela; //Define o modela da tabela
    private int codigoProximaPessoa = 1; //Código do próximo a ser adicionado
    private int linhaSelecionada = -1; 
    private JTextField campoCodigo, campoNome, campoEndereco, campoTelefone; //Cria os campos de texto
    private JTextArea areaAnotacoes; //Cria o campo de anotação

    //Construtor da agenda
    public Agenda() {
        setTitle("Agenda"); //Título da janela
        setSize(1024, 580); //Tamanho da tela
        setDefaultCloseOperation(EXIT_ON_CLOSE); //Indica que vai fechar totalmente o programa
        setResizable(false); //Bloquei o dimensionamento da tela pelo usuário
        montarUI(); //Método
        setVisible(true); //Torna a janela visível
    }

    //Método para criar
    private void montarUI() {
        carregarPessoas(); //Método para carregar pessoas salvas
        setLayout(new BorderLayout()); //Seta o layout Border, que possui 5 direções
        add(new JLabel(new ImageIcon("banner.jpg")), BorderLayout.NORTH); //Carrega a imagem do banner para o norte

        //Adiciona os campos das pessoas na esquerda
        JPanel painelContato = new JPanel(new BorderLayout());
        montarPainelContato(painelContato);
        add(painelContato, BorderLayout.WEST);

        //Adiciona as tabelas das pessoas na direita
        JPanel painelTabela = new JPanel(new BorderLayout());
        montarPainelTabela(painelTabela);
        add(painelTabela, BorderLayout.CENTER);
    }

    /*O método montarPainelContato da classe Agenda cria e organiza dois painéis dentro de um painel principal chamado painelContato. 
    O primeiro painel (painelContatoInfo) mostra detalhes como nome e telefone usando um layout que divide o espaço em diferentes partes. 
    Este painel fica no meio do painelContato. O segundo painel (painelContatoOpcoes) contém botões e outras escolhas relacionadas ao contato, organizados em uma linha na parte de baixo do painelContato. 
    Esse método ajuda a estruturar a interface da aplicação de forma clara, facilitando ver e interagir com as informações de contato. */
    private void montarPainelContato(JPanel painelContato) {
        //Monta onde vão os textos e campos
        JPanel painelContatoInfo = new JPanel(new BorderLayout());
        montarPainelContatoInfo(painelContatoInfo);
        painelContato.add(painelContatoInfo, BorderLayout.CENTER);

        //Monta as opções embaixo
        JPanel painelContatoOpcoes = new JPanel(new FlowLayout());
        montarPainelContatoOpcoes(painelContatoOpcoes);
        painelContato.add(painelContatoOpcoes, BorderLayout.SOUTH);
    }

    private void montarPainelContatoInfo(JPanel painelContatoInfo) {
        JPanel painelContatoInfoCNET = new JPanel(new GridLayout(5, 1)); //Cria o painel de opções 5x1
        painelContatoInfoCNET.add(new JLabel());

        //Adiciona os campos
        campoCodigo = criarCampoComRotulo(painelContatoInfoCNET, "CODIGO     ");
        campoNome = criarCampoComRotulo(painelContatoInfoCNET, "NOME        ");
        campoEndereco = criarCampoComRotulo(painelContatoInfoCNET, "ENDERECO");
        campoTelefone = criarCampoComRotulo(painelContatoInfoCNET, "TELEFONE ");

        painelContatoInfo.add(painelContatoInfoCNET, BorderLayout.WEST);

        JPanel painelContatoInfoAnotacoes = new JPanel(new BorderLayout());
        painelContatoInfoAnotacoes.add(new JLabel("ANOTACOES", JLabel.CENTER), BorderLayout.NORTH);
        areaAnotacoes = new JTextArea(5, 20);
        painelContatoInfoAnotacoes.add(new JScrollPane(areaAnotacoes), BorderLayout.CENTER);

        painelContatoInfo.add(painelContatoInfoAnotacoes, BorderLayout.CENTER);
    }

    /*cria um campo de texto com um rótulo associado em um painel específico passado como parâmetro. Primeiro, ele cria um novo painel (painelCampo) usando um layout de fluxo (FlowLayout).
     Em seguida, adiciona um rótulo (JLabel) com o texto especificado (rotulo) ao painelCampo. Depois, cria um campo de texto (JTextField) com espaço para até 20 caracteres e o adiciona também ao painelCampo.
     Por fim, insere o painelCampo no painel principal (painel) recebido como argumento. O método retorna o campo de texto criado (campo), que pode ser usado posteriormente na aplicação para capturar ou exibir informações.*/
    //Está criando os campos, solicita o painel onde é pra criar e o seu rótulo
     private JTextField criarCampoComRotulo(JPanel painel, String rotulo) {
        JPanel painelCampo = new JPanel(new FlowLayout());
        painelCampo.add(new JLabel(rotulo));
        JTextField campo = new JTextField(20);
        painelCampo.add(campo);
        painel.add(painelCampo);
        return campo;
    }

    //Monta os botões
    private void montarPainelContatoOpcoes(JPanel painelContatoOpcoes) {
        painelContatoOpcoes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //Cria os botões, indicando seu nome e o que fazer quando ele for clicado
        painelContatoOpcoes.add(criarBotao("INSERIR", e -> inserirPessoa()));
        painelContatoOpcoes.add(criarBotao("CONSULTAR", e -> consultarTabela()));
        painelContatoOpcoes.add(criarBotao("ALTERAR", e -> alterarPessoa()));
        painelContatoOpcoes.add(criarBotao("EXCLUIR", e -> excluirPessoa()));
        painelContatoOpcoes.add(criarBotao("LIMPAR", e -> limparCampos()));
    }

    private JButton criarBotao(String texto, ActionListener acao) {
        JButton botao = new JButton(texto);
        botao.addActionListener(acao);
       //Cursor cursor = botao.getCursor();
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //Define o cursor como uma mão ao estar em cima do botão
        return botao;
    }

    /* Método montarPainelTabela da classe Agenda configura um painel que exibe uma tabela de pessoas. Primeiro, define uma margem interna de 10 pixels ao redor do painel (painelTabela).
    Em seguida, cria um modelo de tabela (DefaultTableModel) com colunas específicas: "CODIGO", "NOME", "ENDERECO", "TELEFONE" e "ANOTACOES", inicialmente sem nenhuma linha de dados.
    Depois, cria uma JTable (tabelaPessoas) usando esse modelo de tabela e a adiciona ao painel painelTabela dentro de um componente de rolagem (JScrollPane) na região central (CENTER) do layout.
    Além disso, configura um ouvinte de eventos (MouseListener) na tabela para capturar cliques do mouse.
    Quando o usuário clica em uma linha da tabela, o método verifica qual linha foi selecionada (linhaSelecionada) e, se houver uma linha selecionada, chama o método preencherCampos para exibir os detalhes da pessoa selecionada nos campos correspondentes.
    Por fim, o método atualizarTabela é chamado para garantir que a tabela esteja atualizada com os dados mais recentes. 
    Esse método é crucial para gerenciar e exibir informações de pessoas de maneira organizada e interativa dentro da aplicação. */

    private void montarPainelTabela(JPanel painelTabela) {
        painelTabela.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); //Coloca a borda como 10
        modeloTabela = new DefaultTableModel(new String[]{"CODIGO", "NOME", "ENDERECO", "TELEFONE", "ANOTACOES"}, 0) { //Cria uma tabela com os campos e inicalmente sem nenhuma linha
            public boolean isCellEditable(int linha, int coluna) {
                return false; //Bloqueia a edição direto na tabela
            }
        };

        //Carrega as pessoas existente na tabela
        JTable tabelaPessoas = new JTable(modeloTabela);
        tabelaPessoas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evento) { //Guarda o índice da linha e com os dados dele preenche os campos
                linhaSelecionada = tabelaPessoas.getSelectedRow();
                if (linhaSelecionada != -1) {
                    preencherCampos(linhaSelecionada);
                }
            }
        });
        painelTabela.add(new JScrollPane(tabelaPessoas), BorderLayout.CENTER); //Adiciona um scroll caso a quantidade passe
        atualizarTabela();
    }

    //Preenche os campos com as informações da linha selecionada
    private void preencherCampos(int linha) {
        Pessoa pessoa = pessoas.get(linha);
        campoCodigo.setText(String.valueOf(pessoa.getCodigo()));
        campoNome.setText(pessoa.getNome());
        campoEndereco.setText(pessoa.getEndereco());
        campoTelefone.setText(pessoa.getTelefone());
        areaAnotacoes.setText(pessoa.getAnotacoes());
    }

    //Cria uma nova pessoa com os códigos dos campose, atualiza a tabela, salva no arquivo das pessoas e limpa os campos
    private void inserirPessoa() {
        pessoas.add(new Pessoa(codigoProximaPessoa++, campoNome.getText(), campoEndereco.getText(), campoTelefone.getText(), areaAnotacoes.getText()));
        atualizarTabela();
        salvarPessoas();
        limparCampos();
    }

    //Altera as informações da pessoa selecionada
    private void alterarPessoa() {
        if (linhaSelecionada != -1) {
            Pessoa pessoa = pessoas.get(linhaSelecionada);
            pessoa.setNome(campoNome.getText());
            pessoa.setEndereco(campoEndereco.getText());
            pessoa.setTelefone(campoTelefone.getText());
            pessoa.setAnotacoes(areaAnotacoes.getText());
            atualizarTabela();
            salvarPessoas();
        }
    }

    //Remove os dados da pessoa selecionada
    private void excluirPessoa() {
        if (linhaSelecionada != -1) {
            pessoas.remove(linhaSelecionada);
            atualizarTabela();
            salvarPessoas();
            limparCampos();
        }
    }

    //Limpa os campos quando solicitado
    private void limparCampos() {
        campoCodigo.setText("");
        campoNome.setText("");
        campoEndereco.setText("");
        campoTelefone.setText("");
        areaAnotacoes.setText("");
    }

    //Atualiza as informações das pessoas na tabela quando solicitado
    private void atualizarTabela() {
        modeloTabela.setRowCount(0); //Apaga todas as linhas existentes
        for (Pessoa pessoa : pessoas) { //Para cada pessoa, obtém os dados delas e os imprime na tabela
            modeloTabela.addRow(new Object[]{String.valueOf(pessoa.getCodigo()), pessoa.getNome(), pessoa.getEndereco(), pessoa.getTelefone(), pessoa.getAnotacoes()});
        }
    }

    //Realiza a consulta na tabela a partir de nome ou codigo
    private void consultarTabela() {
        String codigo = campoCodigo.getText(); //Guarda o código
        String nome = campoNome.getText(); //Guarda o nome
        modeloTabela.setRowCount(0); //Seta o início da tabela como 0
        if (!codigo.isEmpty() && !nome.isEmpty()) { //Realiza uma validação se os dois estão digitados
            JFrame Popup = new JFrame();
            JOptionPane.showMessageDialog(Popup, "Você não pode pesquisar por código e nome ao mesmo tempo!");
        } else {
            for (Pessoa pessoa : pessoas) //Loop que itera sobre uma lista de objetos do tipo Pessoa, procurando os valores
            {
                //Busca se existe uma pessoa com esse nome ou código
                if ((!codigo.isEmpty() && pessoa.getCodigo() == Integer.parseInt(codigo))
                || (!nome.isEmpty() && pessoa.getNome().contains(nome))
                || (codigo.isEmpty() && nome.isEmpty())) {
                modeloTabela.addRow(new Object[] { String.valueOf(pessoa.getCodigo()), pessoa.getNome(),
                pessoa.getEndereco(), pessoa.getTelefone(), pessoa.getAnotacoes() });
                }
            }
        }
    }

    private void salvarPessoas() {
        //Guarda as informações de maneira binária
        try (ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream("pessoas.bin"))) {
            objectOut.writeObject(pessoas);
        } catch (IOException erro) {
            System.out.println("Erro ao salvar as pessoas: " + erro.getMessage());
        }
        //Guarda o código da próxima pessoa
        try (ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream("codigo.bin"))) {
            objectOut.writeObject(codigoProximaPessoa);
        } catch (IOException erro) {
            System.out.println("Erro ao salvar o código da próxima pessoa: " + erro.getMessage());
        }
    }

    //Ao abrir a aplicação, carrega as informações existentes
    private void carregarPessoas() {
        try (ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream("pessoas.bin"))) {
            pessoas = (ArrayList<Pessoa>) objectIn.readObject();
        } catch (IOException | ClassNotFoundException erro) {
            System.out.println("Erro ao carregar as pessoas: " + erro.getMessage());
        }
        try (ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream("codigo.bin"))) {
            codigoProximaPessoa = (int) objectIn.readObject();
        } catch (IOException | ClassNotFoundException erro) {
            System.out.println("Erro ao carregar o código da próxima pessoa: " + erro.getMessage());
        }
    }


    public int localizarPessoa(int codigo) {
        for (int i = 0; i < pessoas.size(); i++) {
            if (pessoas.get(i).getCodigo() == codigo) {
                return i;
            }
        }
        return -1; // Vai retornar -1 se não for encontrado. 
    }
}

//Implementa a classe pessoa, que seria as informações binárias
//Serializable informa que pode ser guardado em bytes e depois desconvertido
class Pessoa implements Serializable {
    private int codigo;
    private String nome, endereco, telefone, anotacoes;

    //Construtor das pessoas
    public Pessoa(int codigo, String nome, String endereco, String telefone, String anotacoes) {
        this.codigo = codigo;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.anotacoes = anotacoes;
    }

    //Acessar os valores
    public int getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }
    public String getTelefone() { return telefone; }
    public String getAnotacoes() { return anotacoes; }

    
    public void setNome(String nome) { this.nome = nome; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setAnotacoes(String anotacoes) { this.anotacoes = anotacoes; }
}
