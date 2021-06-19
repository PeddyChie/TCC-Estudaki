package nelson.aparecido.estudaki;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.jaeger.library.StatusBarUtil;

public class DescricaoAtividadeActivity extends AppCompatActivity {


    private View calendario, lupa, home, professor, perfil, btn_me_ajuda;
    private TextView nomeMateria, tipoArquivo, tituloAtividade,descricaoConteudo, dataEntrega, txtUpload;
    private ImageView iconMateria, btnUpload, btnDownload;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String usuarioID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_descricao_atividade);
        StatusBarUtil.setTransparent(this);
        barraDeTarefas();
        cabecalho();

        MaterialAula materialAula = getIntent().getExtras().getParcelable("arquivo"); // <- Objeto contendo o conteúdo selecionado
        AtividadeProva atividadeProva = getIntent().getExtras().getParcelable("atividade"); // <- Objeto contendo o conteúdo selecionado

        DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                tipoArquivo.setText(value.getString("tipoArquivoAtual"));

                if(value.getString("tipoArquivoAtual").equalsIgnoreCase("aula") ||
                        value.getString("tipoArquivoAtual").equalsIgnoreCase("material") ){
                    tituloAtividade.setText(materialAula.getTitulo());
                    descricaoConteudo.setText(materialAula.getDescricao());
                    dataEntrega.setVisibility(View.INVISIBLE);
                    btnUpload.setVisibility(View.INVISIBLE);
                    txtUpload.setVisibility(View.INVISIBLE);

                }else{
                    if(value.getString("ocupacao").equalsIgnoreCase("professor") || value.getString("ocupacao").equalsIgnoreCase("professora") ){
                        btnUpload.setVisibility(View.INVISIBLE);
                        txtUpload.setVisibility(View.INVISIBLE);
                    }
                    tituloAtividade.setText(atividadeProva.getTitulo());
                    descricaoConteudo.setText(atividadeProva.getDescricao());
                    dataEntrega.setText("Data para entrega:"+atividadeProva.getDataMax());

                }
            }
        });

        btnDownload = findViewById(R.id.img_download_descricao_atividade);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot aux, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        if((aux.getString("tipoArquivoAtual").equalsIgnoreCase("aula")) ||
                                (aux.getString("tipoArquivoAtual").equalsIgnoreCase("material"))){
                            gotoURL(materialAula.getUrl());
                        }else{
                            gotoURL(atividadeProva.getUrl());
                        }
                    }
                });
            }
        });
    }

    private void gotoURL(String s) {

        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    private void cabecalho() {
        txtUpload = findViewById(R.id.txt_upload_descricao_atividade);
        btnUpload = findViewById(R.id.img_upload_descricao_atividade);
        tipoArquivo = findViewById(R.id.txt_tipo_arquivo_descricao);
        tituloAtividade = findViewById(R.id.txt_nome_conteudo);
        descricaoConteudo = findViewById(R.id.txt_descricao_conteudo);
        dataEntrega = findViewById(R.id.txt_data_entrega_conteudo);

        nomeMateria = findViewById(R.id.text_nome_descricao_atividade);
        iconMateria = findViewById(R.id.img_descricao_atividade);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if(value.getString("materiaAtual").equalsIgnoreCase("Matemática")){
                    nomeMateria.setText(value.getString("materiaAtual"));
                    Drawable drawable= getResources().getDrawable(R.drawable.logo_matematica);
                    iconMateria.setImageDrawable(drawable);

                }else if(value.getString("materiaAtual").equalsIgnoreCase("Português")){
                    nomeMateria.setText(value.getString("materiaAtual"));
                    Drawable drawable= getResources().getDrawable(R.drawable.logo_portugues);
                    iconMateria.setImageDrawable(drawable);

                }else if(value.getString("materiaAtual").equalsIgnoreCase("Ciências")){
                    nomeMateria.setText(value.getString("materiaAtual"));
                    Drawable drawable= getResources().getDrawable(R.drawable.logo_ciencia);
                    iconMateria.setImageDrawable(drawable);

                }else if(value.getString("materiaAtual").equalsIgnoreCase("Geografia")){
                    nomeMateria.setText(value.getString("materiaAtual"));
                    Drawable drawable= getResources().getDrawable(R.drawable.logo_geografia);
                    iconMateria.setImageDrawable(drawable);

                }else{
                    nomeMateria.setText(value.getString("materiaAtual"));
                    Drawable drawable= getResources().getDrawable(R.drawable.logo_historia);
                    iconMateria.setImageDrawable(drawable);
                }
            }
        });
    }

    private void barraDeTarefas() {

        calendario = findViewById(R.id.view_calendario);
        lupa = findViewById(R.id.view_lupa);
        home = findViewById(R.id.view_home);
        professor = findViewById(R.id.view_conversa_professor);
        perfil = findViewById(R.id.view_perfil);

        btn_me_ajuda = (View) findViewById(R.id.view_me_ajuda_descricao_atividades);
        btn_me_ajuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MeAjudaActivity.class);
                startActivity(intent);
            }
        });

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PerfilActivity.class);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        professor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContatosActivity.class);
                startActivity(intent);
            }
        });

        calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalendarioActivity.class);
                startActivity(intent);
            }
        });

        lupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PesquisaActivity.class);
                startActivity(intent);
            }
        });
    }
}
