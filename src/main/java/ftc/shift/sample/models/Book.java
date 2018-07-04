package ftc.shift.sample.models;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Lombok (https://projectlombok.org/) инструмент, позволяющий не писать геттеры-сеттеры, конструкторы и тд. Они генерируются автоматом.
 * Его использование не является обязательным, но во многих ситуациях заметно упрощает разработку.
 * Так же его использование не отменяет осознанного подхода к инкапсуляции, а именно необходимости открывать доступ к полям (геттеры-сеттеры).
 * Для dto-классов обычно это не распространяется, т.к. они требуют наличия этих методов, а так же equals/hashCode и конструктора
 */

@NoArgsConstructor
@Data
@AllArgsConstructor
public class Book implements Serializable{

    private String id;
    private String name;
    private String author;
    private Integer pages;
    private Boolean isAvailable;
    private List<String> genre;

    public Book(String id, String name, String author, Integer pages, Boolean isAvailable, List<String> genre) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.pages = pages;
        this.isAvailable = isAvailable;
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
