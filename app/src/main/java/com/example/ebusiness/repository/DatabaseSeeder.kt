package com.example.ebusiness.repository

import com.example.ebusiness.entities.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Befüllt die Datenbank beim allerersten App-Start mit Beispieldaten.
 *
 * Warum das hier? Weil wir keine echte Backend-API haben. Statt MockData
 * im Speicher zu halten, schreiben wir die Daten einmalig in Room —
 * danach verhält sich die App so als kämen die Daten von einem Server.
 *
 * Der Seeder läuft nur einmal: wenn die Events-Tabelle leer ist.
 * Bei jedem weiteren App-Start wird er einfach übersprungen.
 */
object DatabaseSeeder {

    /**
     * Einstiegspunkt. Prüft zuerst ob die DB schon Daten hat —
     * wenn ja, wird nichts gemacht (wir wollen keine Duplikate).
     */
    suspend fun seed(db: AppDatabase) {
        // Bereits befüllt → nichts tun
        if (db.eventDao().count() > 0) return

        seedAdressen(db)
        seedEvents(db)
        seedUsers(db)
        seedTickets(db)
        seedLotterie(db)
        seedNotifications(db)
        seedPaymentMethods(db)
    }

    /**
     * Legt alle Veranstaltungsorte an — Deutschland und International.
     * Wir brauchen nur Stadt + Land; Straße ist für unsere Events nicht relevant.
     */
    private suspend fun seedAdressen(db: AppDatabase) {
        val adressen = listOf(
            // Deutschland
            AdresseEntity(id = 1,  city = "Berlin",    country = "Germany"),
            AdresseEntity(id = 2,  city = "Munich",    country = "Germany"),
            AdresseEntity(id = 3,  city = "Nürburg",   country = "Germany"),
            AdresseEntity(id = 4,  city = "Hamburg",   country = "Germany"),
            AdresseEntity(id = 5,  city = "Frankfurt", country = "Germany"),
            AdresseEntity(id = 6,  city = "Dortmund",  country = "Germany"),
            AdresseEntity(id = 7,  city = "Stuttgart", country = "Germany"),
            // International
            AdresseEntity(id = 8,  city = "London",    country = "United Kingdom"),
            AdresseEntity(id = 9,  city = "New York",  country = "USA"),
            AdresseEntity(id = 10, city = "Sydney",    country = "Australia"),
            AdresseEntity(id = 11, city = "Melbourne", country = "Australia"),
            AdresseEntity(id = 12, city = "Paris",     country = "France")
        )
        db.adresseDao().insertAll(adressen)
    }

    /**
     * Legt alle 10 Beispiel-Events an.
     * Die Inhalte kommen aus der ursprünglichen MockData — jetzt aber in der DB.
     * adresseId verweist auf die Orte die wir gerade in seedAdressen() angelegt haben.
     */
    private suspend fun seedEvents(db: AppDatabase) {
        val events = listOf(
            EventEntity(
                id = 1, adresseId = 1,
                title       = "Summer Music Festival",
                description = "The biggest music festival of the summer with over 50 artists on 5 stages. An unforgettable experience for all music fans!",
                organizer   = "LiveNation Events",
                category    = "Music",
                price       = 49.99, capacity = 5000, ticketsSold = 4750,
                hasLottery  = true, imageColor = 0xFF6650A4,
                dateStart   = LocalDate.of(2026, 8, 15),
                dateEnd     = LocalDate.of(2026, 8, 17)
            ),
            EventEntity(
                id = 2, adresseId = 2,
                title       = "NBA Finals Watch Party",
                description = "Live broadcast of the NBA Finals on a big screen. Experience the excitement as if you were in the arena — with snacks, drinks and great atmosphere.",
                organizer   = "SportEvents GmbH",
                category    = "Sports",
                price       = 29.99, capacity = 500, ticketsSold = 420,
                hasLottery  = false, imageColor = 0xFFE65100,
                dateStart   = LocalDate.of(2026, 6, 20),
                dateEnd     = LocalDate.of(2026, 6, 20)
            ),
            EventEntity(
                id = 3, adresseId = 3,
                title       = "Rock am Ring",
                description = "Europe's biggest rock festival. 3 days, 80 bands, unlimited energy. Headliners from around the world make history this year.",
                organizer   = "Rock Events AG",
                category    = "Music",
                price       = 149.99, capacity = 80000, ticketsSold = 79580,
                hasLottery  = true, imageColor = 0xFF1B5E20,
                dateStart   = LocalDate.of(2026, 6, 5),
                dateEnd     = LocalDate.of(2026, 6, 7)
            ),
            EventEntity(
                id = 4, adresseId = 4,
                title       = "Comedy Night XXL",
                description = "The best comedians in Germany on one stage. 3 hours of non-stop laughter — guaranteed!",
                organizer   = "LaughFactory Hamburg",
                category    = "Comedy",
                price       = 24.99, capacity = 800, ticketsSold = 745,
                hasLottery  = false, imageColor = 0xFFB71C1C,
                dateStart   = LocalDate.of(2026, 7, 10),
                dateEnd     = LocalDate.of(2026, 7, 10)
            ),
            EventEntity(
                id = 5, adresseId = 5,
                title       = "Techno Festival 48h",
                description = "48 hours of non-stop techno with international DJs on 4 floors. The hardest electronic event of the year.",
                organizer   = "BeatCulture GmbH",
                category    = "Music",
                price       = 39.99, capacity = 3000, ticketsSold = 2690,
                hasLottery  = true, imageColor = 0xFF0D47A1,
                dateStart   = LocalDate.of(2026, 9, 22),
                dateEnd     = LocalDate.of(2026, 9, 24)
            ),
            EventEntity(
                id = 6, adresseId = 6,
                title       = "Bundesliga Finale",
                description = "The season finale of the Bundesliga. Who will be champion? Be there live when history is written!",
                organizer   = "DFL Sports Events",
                category    = "Sports",
                price       = 79.99, capacity = 81365, ticketsSold = 81175,
                hasLottery  = true, imageColor = 0xFFF57F17,
                dateStart   = LocalDate.of(2026, 5, 18),
                dateEnd     = LocalDate.of(2026, 5, 18)
            ),
            EventEntity(
                id = 7, adresseId = 7,
                title       = "eBusiness Summit 2026",
                description = "Germany's leading conference for digital business models. Keynotes, workshops and networking with top speakers from tech & business.",
                organizer   = "Digital Minds GmbH",
                category    = "Conference",
                price       = 199.99, capacity = 500, ticketsSold = 460,
                hasLottery  = false, imageColor = 0xFF00695C,
                dateStart   = LocalDate.of(2026, 10, 12),
                dateEnd     = LocalDate.of(2026, 10, 13)
            ),
            EventEntity(
                id = 8, adresseId = 1,
                title       = "AI & Innovation Congress",
                description = "Two days of intensive talks on artificial intelligence, automation and the future of work. With live demos and startup pitches.",
                organizer   = "TechVision Events",
                category    = "Conference",
                price       = 249.99, capacity = 1000, ticketsSold = 935,
                hasLottery  = true, imageColor = 0xFF37474F,
                dateStart   = LocalDate.of(2026, 11, 3),
                dateEnd     = LocalDate.of(2026, 11, 4)
            ),
            EventEntity(
                id = 9, adresseId = 2,
                title       = "Hamlet – State Theatre",
                description = "Shakespeare's masterpiece in a modern staging. A gripping journey through power, betrayal and human depths.",
                organizer   = "Bayerische Staatsoper",
                category    = "Theater",
                price       = 59.99, capacity = 700, ticketsSold = 670,
                hasLottery  = false, imageColor = 0xFF4A148C,
                dateStart   = LocalDate.of(2026, 9, 28),
                dateEnd     = LocalDate.of(2026, 9, 28)
            ),
            EventEntity(
                id = 10, adresseId = 4,
                title       = "Cabaret – The Musical",
                description = "The legendary Broadway musical in a spectacular new production. Thrilling songs, breathtaking choreography and a moving story.",
                organizer   = "Stage Entertainment",
                category    = "Theater",
                price       = 74.99, capacity = 1200, ticketsSold = 1080,
                hasLottery  = true, imageColor = 0xFF880E4F,
                dateStart   = LocalDate.of(2026, 10, 14),
                dateEnd     = LocalDate.of(2026, 10, 14)
            ),

            // ── Frankfurt (adresseId = 5) ───────────────────────────────────────
            EventEntity(
                id = 11, adresseId = 5,
                title       = "The Weeknd – After Hours Til Dawn Tour",
                description = "One of the biggest pop & R&B acts of our generation returns to Germany. The Weeknd brings his massive stage production to Deutsche Bank Park for three unforgettable nights.",
                organizer   = "LiveNation Germany",
                category    = "Music",
                price       = 99.99, capacity = 51500, ticketsSold = 50800,
                hasLottery  = true, imageColor = 0xFF1A237E,
                dateStart   = LocalDate.of(2026, 7, 30),
                dateEnd     = LocalDate.of(2026, 8, 1)
            ),
            EventEntity(
                id = 12, adresseId = 5,
                title       = "Rheingau Weinmarkt 2026",
                description = "Germany's most beloved wine festival on Frankfurt's famous Fressgass. Ten days of premium Rheingau wines, regional delicacies, live music and a relaxed summer atmosphere under the open sky.",
                organizer   = "Rheingau Weinwerbung e.V.",
                category    = "Food",
                price       = 12.00, capacity = 8000, ticketsSold = 6200,
                hasLottery  = false, imageColor = 0xFF4E342E,
                dateStart   = LocalDate.of(2026, 9, 2),
                dateEnd     = LocalDate.of(2026, 9, 11)
            ),
            EventEntity(
                id = 13, adresseId = 5,
                title       = "Frankfurter Buchmesse 2026",
                description = "The world's largest trade fair for books and media. Authors, publishers and creative minds from 100+ countries gather in Frankfurt. Public days on the weekend open the fair to all book lovers.",
                organizer   = "Messe Frankfurt GmbH",
                category    = "Conference",
                price       = 19.00, capacity = 20000, ticketsSold = 17500,
                hasLottery  = false, imageColor = 0xFF00695C,
                dateStart   = LocalDate.of(2026, 10, 7),
                dateEnd     = LocalDate.of(2026, 10, 11)
            ),

            // ── Berlin (adresseId = 1) ──────────────────────────────────────────
            EventEntity(
                id = 14, adresseId = 1,
                title       = "Musikfest Berlin 2026",
                description = "One of Europe's most prestigious classical music festivals. World-class orchestras and soloists perform across Berlin's legendary concert halls for nearly four weeks.",
                organizer   = "Berliner Festspiele",
                category    = "Music",
                price       = 55.00, capacity = 2400, ticketsSold = 2100,
                hasLottery  = false, imageColor = 0xFF37474F,
                dateStart   = LocalDate.of(2026, 8, 28),
                dateEnd     = LocalDate.of(2026, 9, 23)
            ),
            EventEntity(
                id = 15, adresseId = 1,
                title       = "52. Berlin-Marathon",
                description = "Run through the heart of Berlin past iconic landmarks like the Brandenburg Gate and the Reichstag. The Berlin Marathon is known as the world's fastest marathon course with multiple world records set here.",
                organizer   = "SCC EVENTS",
                category    = "Sports",
                price       = 140.00, capacity = 55000, ticketsSold = 54100,
                hasLottery  = true, imageColor = 0xFFE65100,
                dateStart   = LocalDate.of(2026, 9, 27),
                dateEnd     = LocalDate.of(2026, 9, 27)
            ),
            EventEntity(
                id = 16, adresseId = 1,
                title       = "IFA Berlin 2026",
                description = "The world's leading trade show for consumer electronics and home appliances. Be among the first to see the latest innovations in smart home, AI devices, audio and mobile technology.",
                organizer   = "Messe Berlin GmbH",
                category    = "Conference",
                price       = 16.00, capacity = 30000, ticketsSold = 24500,
                hasLottery  = false, imageColor = 0xFF0D47A1,
                dateStart   = LocalDate.of(2026, 9, 4),
                dateEnd     = LocalDate.of(2026, 9, 8)
            ),
            EventEntity(
                id = 17, adresseId = 1,
                title       = "Friedrichstadt-Palast Grand Show",
                description = "The world's largest revue theatre presents its new spectacular show. Think Las Vegas meets Berlin — dazzling costumes, massive stage sets, acrobatics and hundreds of performers.",
                organizer   = "Friedrichstadt-Palast Berlin",
                category    = "Theater",
                price       = 69.00, capacity = 1900, ticketsSold = 1650,
                hasLottery  = false, imageColor = 0xFF880E4F,
                dateStart   = LocalDate.of(2026, 10, 1),
                dateEnd     = LocalDate.of(2026, 12, 31)
            ),

            // ── Munich (adresseId = 2) ──────────────────────────────────────────
            EventEntity(
                id = 18, adresseId = 2,
                title       = "Oktoberfest 2026",
                description = "The world-famous Munich Beer Festival — over 6 million visitors, 14 giant beer tents, traditional Bavarian food, fairground rides and live Oompah bands. The ultimate folk festival experience.",
                organizer   = "Landeshauptstadt München",
                category    = "Food",
                price       = 0.0, capacity = 100000, ticketsSold = 87000,
                hasLottery  = false, imageColor = 0xFF1B5E20,
                dateStart   = LocalDate.of(2026, 9, 19),
                dateEnd     = LocalDate.of(2026, 10, 4)
            ),

            // ── London (adresseId = 8) ──────────────────────────────────────────
            EventEntity(
                id = 19, adresseId = 8,
                title       = "Wimbledon Championships 2026",
                description = "The oldest and most prestigious tennis Grand Slam. Watch the world's best players compete on Centre Court's iconic grass. Strawberries, cream and royal box included in the atmosphere.",
                organizer   = "The All England Lawn Tennis Club",
                category    = "Sports",
                price       = 220.00, capacity = 15000, ticketsSold = 14800,
                hasLottery  = true, imageColor = 0xFF1B5E20,
                dateStart   = LocalDate.of(2026, 6, 29),
                dateEnd     = LocalDate.of(2026, 7, 12)
            ),
            EventEntity(
                id = 20, adresseId = 8,
                title       = "BST Hyde Park 2026",
                description = "London's legendary open-air festival in the heart of Hyde Park. World-class headliners across multiple stages with the city skyline as your backdrop. One of the best summer events in Europe.",
                organizer   = "AEG Presents",
                category    = "Music",
                price       = 95.00, capacity = 65000, ticketsSold = 59000,
                hasLottery  = false, imageColor = 0xFF6650A4,
                dateStart   = LocalDate.of(2026, 7, 3),
                dateEnd     = LocalDate.of(2026, 7, 5)
            ),
            EventEntity(
                id = 21, adresseId = 8,
                title       = "Live at the Apollo – Autumn Showcase",
                description = "An evening of world-class stand-up comedy at the legendary Hammersmith Apollo. Multiple headline comedians and surprise guests in one of the most iconic comedy venues on the planet.",
                organizer   = "HAL Productions",
                category    = "Comedy",
                price       = 45.00, capacity = 3500, ticketsSold = 3100,
                hasLottery  = false, imageColor = 0xFFB71C1C,
                dateStart   = LocalDate.of(2026, 10, 15),
                dateEnd     = LocalDate.of(2026, 10, 15)
            ),
            EventEntity(
                id = 22, adresseId = 8,
                title       = "NFL London 2026 – Wembley",
                description = "American Football comes to London! Two regular season NFL games at the iconic Wembley Stadium. Experience the full NFL matchday atmosphere — including tailgate parties and fan zones.",
                organizer   = "NFL International",
                category    = "Sports",
                price       = 130.00, capacity = 86000, ticketsSold = 81000,
                hasLottery  = true, imageColor = 0xFF003069,
                dateStart   = LocalDate.of(2026, 10, 4),
                dateEnd     = LocalDate.of(2026, 10, 4)
            ),
            EventEntity(
                id = 23, adresseId = 8,
                title       = "Les Misérables – West End",
                description = "The world's longest-running musical returns to London's West End in a stunning anniversary production. Victor Hugo's epic story of justice and redemption brought to life with breathtaking sets and performances.",
                organizer   = "Cameron Mackintosh Ltd.",
                category    = "Theater",
                price       = 85.00, capacity = 1800, ticketsSold = 1720,
                hasLottery  = false, imageColor = 0xFF4A148C,
                dateStart   = LocalDate.of(2026, 6, 1),
                dateEnd     = LocalDate.of(2026, 12, 31)
            ),

            // ── New York City (adresseId = 9) ───────────────────────────────────
            EventEntity(
                id = 24, adresseId = 9,
                title       = "FIFA World Cup 2026 – Round of 16",
                description = "History in the making! The FIFA World Cup comes to the Americas for the first time since 1994. Watch a Round of 16 clash at MetLife Stadium — one of the biggest stadiums on the planet.",
                organizer   = "FIFA / US Soccer Federation",
                category    = "Sports",
                price       = 350.00, capacity = 82500, ticketsSold = 82200,
                hasLottery  = true, imageColor = 0xFFE65100,
                dateStart   = LocalDate.of(2026, 7, 5),
                dateEnd     = LocalDate.of(2026, 7, 5)
            ),
            EventEntity(
                id = 25, adresseId = 9,
                title       = "US Open Tennis 2026",
                description = "The final Grand Slam of the year at Flushing Meadows. Night sessions under the lights at Arthur Ashe Stadium are electric. The US Open is tennis at its loudest, most thrilling best.",
                organizer   = "USTA",
                category    = "Sports",
                price       = 180.00, capacity = 23771, ticketsSold = 22000,
                hasLottery  = false, imageColor = 0xFF0D47A1,
                dateStart   = LocalDate.of(2026, 8, 31),
                dateEnd     = LocalDate.of(2026, 9, 13)
            ),
            EventEntity(
                id = 26, adresseId = 9,
                title       = "Ariana Grande – NYC Arena Concert",
                description = "Pop superstar Ariana Grande brings her blockbuster world tour to New York City. Expect stunning production, hits spanning her entire career and an atmosphere that only NYC can deliver.",
                organizer   = "Republic Records / LiveNation",
                category    = "Music",
                price       = 145.00, capacity = 20000, ticketsSold = 19500,
                hasLottery  = true, imageColor = 0xFF6650A4,
                dateStart   = LocalDate.of(2026, 7, 18),
                dateEnd     = LocalDate.of(2026, 7, 18)
            ),
            EventEntity(
                id = 27, adresseId = 9,
                title       = "Nathan's Hot Dog Eating Contest",
                description = "The world's most famous competitive eating event on Coney Island — a July 4th tradition since 1916. Watch world-record holders compete for the coveted mustard yellow belt live on the boardwalk.",
                organizer   = "Nathan's Famous Inc.",
                category    = "Food",
                price       = 0.0, capacity = 35000, ticketsSold = 30000,
                hasLottery  = false, imageColor = 0xFFB71C1C,
                dateStart   = LocalDate.of(2026, 7, 4),
                dateEnd     = LocalDate.of(2026, 7, 4)
            ),
            EventEntity(
                id = 28, adresseId = 9,
                title       = "Hamilton – Broadway",
                description = "The phenomenon that changed musical theatre. Lin-Manuel Miranda's masterpiece tells the story of American Founding Father Alexander Hamilton through hip-hop, R&B and show tunes at the Richard Rodgers Theatre.",
                organizer   = "Jeffrey Seller Productions",
                category    = "Theater",
                price       = 199.00, capacity = 1319, ticketsSold = 1290,
                hasLottery  = true, imageColor = 0xFF880E4F,
                dateStart   = LocalDate.of(2026, 6, 1),
                dateEnd     = LocalDate.of(2026, 12, 31)
            ),

            // ── Sydney (adresseId = 10) ─────────────────────────────────────────
            EventEntity(
                id = 29, adresseId = 10,
                title       = "Lord of the Rings in Concert",
                description = "Experience Peter Jackson's epic trilogy live in the stunning Darling Harbour Theatre. A full symphony orchestra and choir perform Howard Shore's legendary score while the entire film plays on the big screen.",
                organizer   = "Touring Consortium Theatre Company",
                category    = "Music",
                price       = 89.00, capacity = 3500, ticketsSold = 3200,
                hasLottery  = false, imageColor = 0xFF37474F,
                dateStart   = LocalDate.of(2026, 10, 7),
                dateEnd     = LocalDate.of(2026, 10, 8)
            ),
            EventEntity(
                id = 30, adresseId = 10,
                title       = "Sydney Harbour Night Market",
                description = "Sydney's most scenic food festival with over 100 street food vendors, live music and craft bars — all with a stunning view of the Harbour Bridge and Opera House. The ultimate Sydney outdoor dining experience.",
                organizer   = "Night Markets Australia",
                category    = "Food",
                price       = 5.00, capacity = 5000, ticketsSold = 3800,
                hasLottery  = false, imageColor = 0xFF00695C,
                dateStart   = LocalDate.of(2026, 10, 16),
                dateEnd     = LocalDate.of(2026, 11, 20)
            ),
            EventEntity(
                id = 31, adresseId = 10,
                title       = "Beauty Expo Australia 2026",
                description = "Australasia's largest professional beauty industry event at the ICC Sydney. Over 200 exhibitors showcasing the latest in skincare, cosmetics, wellness and salon technology. Open to professionals and the public.",
                organizer   = "Reed Exhibitions Australia",
                category    = "Conference",
                price       = 35.00, capacity = 10000, ticketsSold = 7800,
                hasLottery  = false, imageColor = 0xFFAD1457,
                dateStart   = LocalDate.of(2026, 8, 22),
                dateEnd     = LocalDate.of(2026, 8, 24)
            ),
            EventEntity(
                id = 32, adresseId = 10,
                title       = "Sydney Opera House – Classics Season",
                description = "A full season of world-class opera, ballet and orchestral performances at one of the most iconic buildings on earth. Whether you're a first-timer or a regular, the Sydney Opera House never disappoints.",
                organizer   = "Sydney Opera House Trust",
                category    = "Theater",
                price       = 95.00, capacity = 2679, ticketsSold = 2200,
                hasLottery  = false, imageColor = 0xFF4A148C,
                dateStart   = LocalDate.of(2026, 7, 1),
                dateEnd     = LocalDate.of(2026, 12, 20)
            ),

            // ── Melbourne (adresseId = 11) ──────────────────────────────────────
            EventEntity(
                id = 33, adresseId = 11,
                title       = "Hans Zimmer Live – The Next Level",
                description = "The legendary composer of The Lion King, Inception, Interstellar and Dune takes the stage at Rod Laver Arena. An extraordinary concert experience with a massive orchestra, choir and stunning visuals.",
                organizer   = "DCP Entertainment",
                category    = "Music",
                price       = 159.00, capacity = 14820, ticketsSold = 14500,
                hasLottery  = true, imageColor = 0xFF1A237E,
                dateStart   = LocalDate.of(2026, 10, 24),
                dateEnd     = LocalDate.of(2026, 10, 25)
            ),
            EventEntity(
                id = 34, adresseId = 11,
                title       = "BABYMONSTER World Tour 2026",
                description = "The hottest new K-Pop act on the planet brings their debut world tour to Melbourne's Rod Laver Arena. Explosive choreography, tight vocals and an energy that has to be seen to be believed.",
                organizer   = "YG Entertainment / Frontier Touring",
                category    = "Music",
                price       = 120.00, capacity = 14820, ticketsSold = 13900,
                hasLottery  = true, imageColor = 0xFF880E4F,
                dateStart   = LocalDate.of(2026, 12, 11),
                dateEnd     = LocalDate.of(2026, 12, 11)
            ),
            EventEntity(
                id = 35, adresseId = 11,
                title       = "Melbourne Cup Carnival 2026",
                description = "The race that stops a nation. The Melbourne Cup Carnival is four days of world-class thoroughbred racing, fashion, fine dining and entertainment at Flemington Racecourse. Australia's greatest sporting tradition.",
                organizer   = "Victoria Racing Club",
                category    = "Sports",
                price       = 75.00, capacity = 100000, ticketsSold = 88000,
                hasLottery  = false, imageColor = 0xFFF57F17,
                dateStart   = LocalDate.of(2026, 10, 29),
                dateEnd     = LocalDate.of(2026, 11, 7)
            ),
            EventEntity(
                id = 36, adresseId = 11,
                title       = "The Book of Mormon – Melbourne",
                description = "The outrageously funny, award-winning musical from the creators of South Park. Winner of 9 Tony Awards, this satirical comedy has taken the world by storm. Not for the easily offended — which makes it even better.",
                organizer   = "James Nederlander / Anne Garefino",
                category    = "Theater",
                price       = 99.00, capacity = 2000, ticketsSold = 1870,
                hasLottery  = false, imageColor = 0xFF4A148C,
                dateStart   = LocalDate.of(2026, 7, 10),
                dateEnd     = LocalDate.of(2026, 8, 30)
            ),

            // ── Paris (adresseId = 12) ──────────────────────────────────────────
            EventEntity(
                id = 37, adresseId = 12,
                title       = "Rock en Seine Festival 2026",
                description = "One of Europe's finest rock and indie festivals set in the spectacular grounds of the Domaine National de Saint-Cloud on the outskirts of Paris. Three days, multiple stages, world-class headliners and a beautiful Seine valley backdrop.",
                organizer   = "Rock en Seine SARL",
                category    = "Music",
                price       = 85.00, capacity = 40000, ticketsSold = 35000,
                hasLottery  = false, imageColor = 0xFF6650A4,
                dateStart   = LocalDate.of(2026, 8, 28),
                dateEnd     = LocalDate.of(2026, 8, 30)
            ),
            EventEntity(
                id = 38, adresseId = 12,
                title       = "Orelsan – Accor Arena Residency",
                description = "France's biggest rap artist performs 10 consecutive sold-out nights at the Accor Arena in Paris. An epic, intimate residency from the man who redefined French rap — expect a career-spanning setlist and unmatched production.",
                organizer   = "Wagram Music / LiveNation France",
                category    = "Music",
                price       = 75.00, capacity = 20000, ticketsSold = 19800,
                hasLottery  = true, imageColor = 0xFF1B5E20,
                dateStart   = LocalDate.of(2026, 12, 9),
                dateEnd     = LocalDate.of(2026, 12, 20)
            ),
            EventEntity(
                id = 39, adresseId = 12,
                title       = "Paris Masters Tennis 2026",
                description = "The final ATP Masters 1000 event of the year at the Accor Arena. Top-ranked players compete indoors on a fast hardcourt in one of tennis' most prestigious settings — the perfect warm-up before the season finale.",
                organizer   = "ATP / Fédération Française de Tennis",
                category    = "Sports",
                price       = 90.00, capacity = 15000, ticketsSold = 13000,
                hasLottery  = false, imageColor = 0xFF0D47A1,
                dateStart   = LocalDate.of(2026, 10, 26),
                dateEnd     = LocalDate.of(2026, 11, 1)
            ),
            EventEntity(
                id = 40, adresseId = 12,
                title       = "Paradis sur Seine – Gala 2026",
                description = "An exclusive black-tie dinner cruise along the Seine on Bastille Day eve. Fine French cuisine, live jazz, panoramic views of illuminated Paris monuments — and a front-row seat to the midnight fireworks over the Eiffel Tower.",
                organizer   = "Bateaux Parisiens Events",
                category    = "Food",
                price       = 280.00, capacity = 300, ticketsSold = 265,
                hasLottery  = true, imageColor = 0xFF4E342E,
                dateStart   = LocalDate.of(2026, 7, 13),
                dateEnd     = LocalDate.of(2026, 7, 13)
            ),
            EventEntity(
                id = 41, adresseId = 12,
                title       = "Mondial de l'Auto – Paris Motor Show",
                description = "One of the world's most important motor shows returns to Paris Expo Porte de Versailles. Global car manufacturers unveil their latest models and concept cars. A must for any car enthusiast — from EVs to supercars.",
                organizer   = "Comité des Constructeurs Français d'Automobiles",
                category    = "Conference",
                price       = 18.00, capacity = 50000, ticketsSold = 41000,
                hasLottery  = false, imageColor = 0xFF37474F,
                dateStart   = LocalDate.of(2026, 10, 15),
                dateEnd     = LocalDate.of(2026, 10, 25)
            ),
            EventEntity(
                id = 42, adresseId = 12,
                title       = "Cinéma Paradiso Louvre",
                description = "Open-air cinema and live theater performances set inside the breathtaking Cour Carrée of the Louvre palace. Watch classic films and live performances with one of history's most beautiful buildings as your backdrop.",
                organizer   = "Musée du Louvre / Pathé Live",
                category    = "Theater",
                price       = 45.00, capacity = 2000, ticketsSold = 1750,
                hasLottery  = false, imageColor = 0xFF880E4F,
                dateStart   = LocalDate.of(2026, 7, 10),
                dateEnd     = LocalDate.of(2026, 7, 31)
            )
        )
        db.eventDao().insertAll(events)
    }

    /**
     * Legt zwei Demo-User an — Fan und Host.
     *
     * name und email bleiben leer (Datenschutz-Regel: keine echten Daten).
     * displayName, phone, location sind Demo-Werte, die der User später
     * im EditProfileScreen überschreiben kann.
     * Credits startet bei 50€ als Willkommensbonus für den Fan-Account.
     */
    private suspend fun seedUsers(db: AppDatabase) {
        db.userDao().insert(
            UserEntity(
                id          = 1,
                name        = "",           // echte Daten nie hier speichern
                email       = "",           // echte Daten nie hier speichern
                userType    = "fan",
                credits     = 50.0,
                currency    = "EUR",
                displayName = "Music Fan",
                phone       = "+49 170 123 4567",
                location    = "Berlin, Germany",
                memberSince = "2024"
            )
        )

        db.userDao().insert(
            UserEntity(
                id          = 2,
                name        = "",
                email       = "",
                userType    = "host",
                credits     = 0.0,
                currency    = "EUR",
                displayName = "Event Host Pro",
                phone       = "+49 89 987 6543",
                location    = "Munich, Germany",
                memberSince = "2023"
            )
        )
    }

    /**
     * Legt Demo-Benachrichtigungen für den Fan-Account an.
     * Die Inhalte spiegeln die Lotterie-Ergebnisse aus seedLotterie() wider.
     */
    private suspend fun seedNotifications(db: AppDatabase) {
        val today = LocalDate.now()
        val fmt   = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        db.notificationDao().insertAll(listOf(

            // Ariana Grande – Lotterie noch offen (Pending)
            NotificationEntity(
                id          = 1,
                userId      = 1,
                type        = "lottery_pending",
                title       = "Lottery Entry Confirmed 🎟",
                message     = "You've entered the lottery for Ariana Grande – NYC Arena Concert. Results will be announced soon!",
                isRead      = false,
                createdAt   = today.minusDays(1).format(fmt),
                actionLabel = "View Entry"
            ),

            // Hamilton – gewonnen (Won) → eventId=2 (Hamilton)
            NotificationEntity(
                id           = 2,
                userId       = 1,
                type         = "lottery_win",
                title        = "Lottery Win! 🎉",
                message      = "Congratulations! You won 2 lottery tickets for Hamilton – Broadway in New York City.",
                isRead       = false,
                createdAt    = today.minusDays(2).format(fmt),
                actionLabel  = "Claim Ticket",
                eventId      = 2
            ),

            // Orelsan – verloren (Lost) → 25% cashback = 0.27
            NotificationEntity(
                id           = 3,
                userId       = 1,
                type         = "lottery_lose",
                title        = "Lottery Result",
                message      = "Unfortunately you didn't win the lottery for Orelsan – Accor Arena Residency. Get your 25% cashback now!",
                isRead       = false,
                createdAt    = today.minusDays(2).format(fmt),
                actionLabel  = "Claim \$0.27 Cashback",
                actionAmount = 0.27
            ),

            // Event Reminder
            NotificationEntity(
                id          = 4,
                userId      = 1,
                type        = "reminder",
                title       = "Event Reminder",
                message     = "The Weeknd – After Hours Til Dawn Tour is in 5 days! Don't forget your ticket.",
                isRead      = true,
                createdAt   = today.minusDays(5).format(fmt),
                actionLabel = null
            ),

            // Credits
            NotificationEntity(
                id          = 5,
                userId      = 1,
                type        = "credits",
                title       = "Credits Earned",
                message     = "You received \$5.00 credits as a welcome bonus. Use them for lottery entries!",
                isRead      = true,
                createdAt   = today.minusDays(30).format(fmt),
                actionLabel = null
            )
        ))
    }

    /**
     * Legt zwei Demo-Zahlungsmethoden für den Fan-Account an.
     */
    private suspend fun seedPaymentMethods(db: AppDatabase) {
        db.paymentMethodDao().insertAll(listOf(
            PaymentMethodEntity(
                id         = 1,
                userId     = 1,
                name       = "PayPal",
                type       = "paypal",
                emoji      = "💳",
                isVerified = true
            ),
            PaymentMethodEntity(
                id         = 2,
                userId     = 1,
                name       = "Visa •••• 4242",
                type       = "card",
                emoji      = "💳",
                isVerified = true
            )
        ))
    }

    /**
     * Legt 7 Demo-Tickets für den Fan-Account an — aus echten Events.
     * Mix aus Active (bevorstehend), Past (schon vorbei) und Lottery (Lotterie-Status).
     * Die Daten (Titel, Datum, Ort, Preis) passen 1:1 zu den Events in seedEvents().
     */
    private suspend fun seedTickets(db: AppDatabase) {
        db.ticketDao().insertAll(listOf(

            // ── ACTIVE – bevorstehende Events ────────────────────────────────────

            // The Weeknd Tour – Frankfurt, 30. Juli 2026
            TicketEntity(
                id            = 1,
                eventId       = 11,
                userId        = 1,
                eventTitle    = "The Weeknd – After Hours Til Dawn Tour",
                eventDate     = "Thu, Jul 30, 2026",
                eventLocation = "Frankfurt, Germany",
                seat          = "34",
                section       = "Floor",
                price         = 99.99,
                status        = "Active",
                qrCode        = "SP-11-WEEKND-FRA-A034",
                purchaseDate  = LocalDate.of(2026, 1, 15)
            ),

            // Wimbledon – London, Finals Day 12. Juli 2026
            TicketEntity(
                id            = 2,
                eventId       = 19,
                userId        = 1,
                eventTitle    = "Wimbledon Championships 2026",
                eventDate     = "Sun, Jul 12, 2026",
                eventLocation = "London, United Kingdom",
                seat          = "112",
                section       = "Centre Court",
                price         = 220.00,
                status        = "Active",
                qrCode        = "SP-19-WIMBLEDON-CC112",
                purchaseDate  = LocalDate.of(2026, 2, 3)
            ),

            // Hans Zimmer – Melbourne, 24. Oktober 2026
            TicketEntity(
                id            = 3,
                eventId       = 33,
                userId        = 1,
                eventTitle    = "Hans Zimmer Live – The Next Level",
                eventDate     = "Sat, Oct 24, 2026",
                eventLocation = "Melbourne, Australia",
                seat          = "88",
                section       = "Golden Circle",
                price         = 159.00,
                status        = "Active",
                qrCode        = "SP-33-HANSZIMMER-MEL-G088",
                purchaseDate  = LocalDate.of(2026, 3, 20)
            ),

            // Rock en Seine – Paris, 28. August 2026
            TicketEntity(
                id            = 4,
                eventId       = 37,
                userId        = 1,
                eventTitle    = "Rock en Seine Festival 2026",
                eventDate     = "Fri, Aug 28, 2026",
                eventLocation = "Paris, France",
                seat          = "–",
                section       = "General Admission",
                price         = 85.00,
                status        = "Active",
                qrCode        = "SP-37-ROCKENSEINE-GA-4471",
                purchaseDate  = LocalDate.of(2026, 4, 5)
            ),

            // ── PAST – bereits vergangene Events ────────────────────────────────

            // Bundesliga Finale – Dortmund, 18. Mai 2026
            TicketEntity(
                id            = 5,
                eventId       = 6,
                userId        = 1,
                eventTitle    = "Bundesliga Finale",
                eventDate     = "Mon, May 18, 2026",
                eventLocation = "Dortmund, Germany",
                seat          = "206",
                section       = "Südtribüne",
                price         = 79.99,
                status        = "Past",
                qrCode        = "SP-6-BL-FINALE-S206",
                purchaseDate  = LocalDate.of(2026, 1, 28)
            ),

            // Rock am Ring – Nürburg, 6. Juni 2026
            TicketEntity(
                id            = 6,
                eventId       = 3,
                userId        = 1,
                eventTitle    = "Rock am Ring",
                eventDate     = "Sat, Jun 6, 2026",
                eventLocation = "Nürburg, Germany",
                seat          = "–",
                section       = "General Admission",
                price         = 149.99,
                status        = "Past",
                qrCode        = "SP-3-ROCKAMI-GA-8821",
                purchaseDate  = LocalDate.of(2025, 11, 12)
            ),

            // ── LOTTERY – Ticket über Lotterie erworben ──────────────────────────

            // FIFA World Cup R16 – New York, 5. Juli 2026
            TicketEntity(
                id            = 7,
                eventId       = 24,
                userId        = 1,
                eventTitle    = "FIFA World Cup 2026 – Round of 16",
                eventDate     = "Sun, Jul 5, 2026",
                eventLocation = "New York, USA",
                seat          = "414",
                section       = "East Stand",
                price         = 350.00,
                status        = "Lottery",
                qrCode        = "SP-24-FIFA-WC-R16-E414",
                purchaseDate  = LocalDate.of(2026, 5, 1)
            )
        ))
    }

    /**
     * Legt drei Demo-Lotterie-Einträge an — für echte Events mit hasLottery = true.
     * Pending = Ergebnis steht noch aus, Won = gewonnen, Lost = leider nicht.
     */
    private suspend fun seedLotterie(db: AppDatabase) {
        db.lotteryDao().insertAll(listOf(

            // Ariana Grande NYC – noch offen (Pending)
            LotteryEntryEntity(
                id          = 1,
                eventId     = 26,          // Ariana Grande – NYC Arena Concert
                userId      = 1,
                entryDate   = LocalDate.of(2026, 5, 20),
                status      = "Pending",   // Ziehung noch nicht erfolgt
                ticketsPaid = 1,
                amountPaid  = 1.09
            ),

            // Hamilton Broadway NYC – gewonnen (Won)
            LotteryEntryEntity(
                id          = 2,
                eventId     = 28,          // Hamilton – Broadway
                userId      = 1,
                entryDate   = LocalDate.of(2026, 4, 2),
                status      = "Won",       // Glück gehabt!
                ticketsPaid = 2,
                amountPaid  = 2.18
            ),

            // Orelsan Paris – verloren (Lost)
            LotteryEntryEntity(
                id          = 3,
                eventId     = 38,          // Orelsan – Accor Arena Residency
                userId      = 1,
                entryDate   = LocalDate.of(2026, 6, 1),
                status      = "Lost",      // Leider kein Glück
                ticketsPaid = 1,
                amountPaid  = 1.09
            )
        ))
    }
}
