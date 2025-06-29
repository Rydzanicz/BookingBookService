package com.example.BookingBookService.service;

import com.example.BookingBookService.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class GoogleBooksServiceTest {

    @Value("fakeGoogleApiKey")
    private final String apiKey = "fakeApiKey";
    private GoogleBooksService googleBooksService;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        googleBooksService = new GoogleBooksService(restTemplate, apiKey);
    }

    @Test
    void searchBooksShouldMapJsonToBooksCorrectly() {

        //When
        final String jsonResponse = """
                                   {
                "kind" : "books#volumes",
                "totalItems" : 1000000,
                "items" : [ {
                  "kind" : "books#volume",
                  "id" : "n3vng7gyGCYC",
                  "etag" : "CxgBHAedf7M",
                  "selfLink" : "https://www.googleapis.com/books/v1/volumes/n3vng7gyGCYC",
                  "volumeInfo" : {
                    "title" : "Harry Potter",
                    "publisher" : "PediaPress",
                    "readingModes" : {
                      "text" : false,
                      "image" : true
                    },
                    "pageCount" : 1011,
                    "printType" : "BOOK",
                    "maturityRating" : "NOT_MATURE",
                    "allowAnonLogging" : false,
                    "contentVersion" : "0.1.1.0.preview.1",
                    "panelizationSummary" : {
                      "containsEpubBubbles" : false,
                      "containsImageBubbles" : false
                    },
                    "imageLinks" : {
                      "smallThumbnail" : "http://books.google.com/books/content?id=n3vng7gyGCYC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",
                      "thumbnail" : "http://books.google.com/books/content?id=n3vng7gyGCYC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
                    },
                    "language" : "en",
                    "previewLink" : "http://books.google.pl/books?id=n3vng7gyGCYC&pg=PA857&dq=harry%2520potter&hl=&cd=1&source=gbs_api",
                    "infoLink" : "http://books.google.pl/books?id=n3vng7gyGCYC&dq=harry%2520potter&hl=&source=gbs_api",
                    "canonicalVolumeLink" : "https://books.google.com/books/about/Harry_Potter.html?hl=&id=n3vng7gyGCYC"
                  },
                  "saleInfo" : {
                    "country" : "PL",
                    "saleability" : "NOT_FOR_SALE",
                    "isEbook" : false
                  },
                  "accessInfo" : {
                    "country" : "PL",
                    "viewability" : "PARTIAL",
                    "embeddable" : true,
                    "publicDomain" : false,
                    "textToSpeechPermission" : "ALLOWED",
                    "epub" : {
                      "isAvailable" : false
                    },
                    "pdf" : {
                      "isAvailable" : true,
                      "acsTokenLink" : "http://books.google.pl/books/download/Harry_Potter-sample-pdf.acsm?id=n3vng7gyGCYC&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api"
                    },
                    "webReaderLink" : "http://play.google.com/books/reader?id=n3vng7gyGCYC&hl=&source=gbs_api",
                    "accessViewStatus" : "SAMPLE",
                    "quoteSharingAllowed" : false
                  },
                  "searchInfo" : {
                    "textSnippet" : "1155 &quot; <b>Harry Potter</b> and the Sorcerer&#39;s Stone ( PSX ) &quot; http://www.metacritic.com/games/platforms/ psx / harrypotterandthesorcerersstone ? q = <b>Harry</b> % <b>20Potter</b> % 20and % 20the % 20Sorcerer % 27s % 20Stone . CBS Interactive Inc&nbsp;..."
                  }
                }]}
                """;

        final String query = "ASC";
        final String url = "https://www.googleapis.com/books/v1/volumes?q=ASC&startIndex=0&maxResults=10&key=" + apiKey;

        mockServer.expect(requestTo(url)).andExpect(request -> assertEquals(HttpMethod.GET, request.getMethod()))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // When
        final Page<Book> books = googleBooksService.searchBooks(query, 0, 10);

        // Then
        assertNotNull(books);
        assertEquals(1, books.getContent().size());

        Book book1 = books.getContent().get(0);

        assertEquals("n3vng7gyGCYC", book1.getGoogleBookId());
        assertEquals("Harry Potter", book1.getTitle());
        assertEquals(null, book1.getPublishedDate());
        assertEquals(null, book1.getDescription());
        assertEquals("http://books.google.pl/books/download/Harry_Potter-sample-pdf.acsm?id=n3vng7gyGCYC&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api", book1.getPdfAcsTokenLink());
        mockServer.verify();
    }

    @Test
    void searchBooksShouldMapJsonToBooksCorrectlyWhenPDFIsEmpty() {

        //When
        final String jsonResponse = """
                    {
                        "kind": "books#volumes",
                        "totalItems": 1000000,
                        "items": [
                            {
                                "id": "ZhQ9kYNnrVoC",
                                "volumeInfo": {
                                    "title": "ASC election",
                                    "authors": ["United States. Agricultural Stabilization and Conservation Service"],
                                    "publishedDate": "1980",
                                    "description": "A sample description for a book."
                                }
                            },
                            {
                                "id": "jOhXEQAAQBAJ",
                                "volumeInfo": {
                                    "title": "ASC and AI. A Dialogue Between an ASC Master, Psychologist, and AI",
                                    "authors": ["Sergey Kravchenko"],
                                    "publishedDate": "2025-04-17",
                                    "description": "Exploring the intersection of ASC and AI."
                                }
                            }
                        ]
                    }
                """;

        final String query = "ASC";
        final String url = "https://www.googleapis.com/books/v1/volumes?q=ASC&startIndex=0&maxResults=10&key=" + apiKey;

        mockServer.expect(requestTo(url)).andExpect(request -> assertEquals(HttpMethod.GET, request.getMethod()))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // When
        final Page<Book> books = googleBooksService.searchBooks(query, 0, 10);

        // Then
        assertNotNull(books);
        assertEquals(2, books.getContent().size());

        Book book1 = books.getContent().get(0);

        assertEquals("ZhQ9kYNnrVoC", book1.getGoogleBookId());
        assertEquals("ASC election", book1.getTitle());
        assertEquals(1, book1.getAuthors().size());
        assertEquals("United States. Agricultural Stabilization and Conservation Service", book1.getAuthors().get(0));
        assertEquals("1980", book1.getPublishedDate());
        assertEquals("A sample description for a book.", book1.getDescription());
        assertEquals(null, book1.getPdfAcsTokenLink());

        Book book2 = books.getContent().get(1);
        assertEquals("jOhXEQAAQBAJ", book2.getGoogleBookId());
        assertEquals("ASC and AI. A Dialogue Between an ASC Master, Psychologist, and AI", book2.getTitle());
        assertEquals(1, book2.getAuthors().size());
        assertEquals("Sergey Kravchenko", book2.getAuthors().get(0));
        assertEquals("2025-04-17", book2.getPublishedDate());
        assertEquals("Exploring the intersection of ASC and AI.", book2.getDescription());

        mockServer.verify();
    }

    @Test
    void searchBooksShouldHandlePagination() {
        // Given
        final String jsonResponse = """
                {
                    "kind": "books#volumes",
                    "totalItems": 100,
                    "items": [{
                        "id": "pageTestId",
                        "volumeInfo": {
                            "title": "Test Book on Page 2"
                        }
                    }]
                }
                """;

        final String query = "TestQuery";
        final int page = 2;
        final int size = 10;
        final String url = String.format(
                "https://www.googleapis.com/books/v1/volumes?q=%s&startIndex=%d&maxResults=%d&key=%s",
                query, page * size, size, apiKey
        );

        mockServer.expect(requestTo(url))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // When
        final Page<Book> books = googleBooksService.searchBooks(query, page, size);

        // Then
        assertNotNull(books);
        assertEquals(1, books.getContent().size());
        assertEquals(page, books.getNumber());
        assertEquals(size, books.getSize());
        assertEquals(100, books.getTotalElements());
        mockServer.verify();
    }

}