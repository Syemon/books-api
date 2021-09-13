/*
 * Copyright (C) 2021  Karan Kumar
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.booksapi.model;

import com.karankumar.booksapi.model.cover.BookCover;
import com.karankumar.booksapi.model.format.Format;
import com.karankumar.booksapi.model.genre.GenreName;
import com.karankumar.booksapi.model.language.LanguageName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(
                    name = "book_id",
                    foreignKey = @ForeignKey(name = "book_author_book_id_fk")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "author_id",
                    foreignKey = @ForeignKey(name = "book_author_author_id_fk")
            )
    )
    private Set<Author> authors = new HashSet<>();

    @Column(nullable = false)
    private LanguageName languageName;

    private String isbn10;

    private String isbn13;

    @Column(nullable = false)
    private GenreName genre;

    private Integer yearOfPublication;

    @Column(nullable = false)
    private String blurb;

    @ToString.Exclude
    @ManyToMany(mappedBy = "books", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Publisher> publishers = new HashSet<>();

    // One to one because a different book format warrants a new ISBN, so we will classify it as a
    // new book/edition
    @OneToOne
    private Format format;

    @ToString.Exclude
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private Set<BookCover> bookCover = new HashSet<>();

    public Book(@NonNull String title, @NonNull LanguageName languageName, @NonNull String blurb,
                @NonNull GenreName genre, @NonNull Format format) {
        this.title = title;
        this.languageName = languageName;
        this.blurb = blurb;
        this.genre = genre;
        this.format = format;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
