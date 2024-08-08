package br.com.alura.screenmatch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "episodios")
public class Episodio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "avaliacao")
    private String avaliacao;

    @Column(name = "data_lancamento")
    private String dataLancamento;

    @ManyToOne
    @JoinColumn(name = "id_serie")
    private Serie serie;

    public Episodio() {
    }

    public Episodio(DadosEpisodios dadosEpisodios) {
        this.titulo = dadosEpisodios.titulo();
        this.numero = dadosEpisodios.numero();
        this.avaliacao = dadosEpisodios.avaliacao();
        this.dataLancamento = dadosEpisodios.dataLancamento();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(String dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

}
