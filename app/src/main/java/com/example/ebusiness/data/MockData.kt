package com.example.ebusiness.data


data class Event(
    val id: Int,
    val title: String,
    val location: String,
    val date: String,
    val price: Double,
    val category: String,
    val hasLottery: Boolean,
    val imageColor: Long,
    val description: String,
    val organizer: String = "Event Organizer",
    val ticketsLeft: Int = 100
)

data class Ticket(
    val id: Int,
    val eventId: Int,
    val eventTitle: String,
    val eventDate: String,
    val eventLocation: String,
    val seat: String,
    val qrCode: String,
    val section: String = "A",
    val price: Double = 0.0,
    val status: String = "Active"   // "Active", "Past", "Lottery"
)


object MockData {

    val events = listOf(
        Event(
            id = 1,
            title = "Summer Music Festival",
            location = "Berlin, Germany",
            date = "Aug 15, 2025",
            price = 49.99,
            category = "Music",
            hasLottery = true,
            imageColor = 0xFF6650A4,
            description = "The biggest music festival of the summer with over 50 artists on 5 stages. An unforgettable experience for all music fans!",
            organizer = "LiveNation Events",
            ticketsLeft = 250
        ),
        Event(
            id = 2,
            title = "NBA Finals Watch Party",
            location = "Munich, Germany",
            date = "Jun 20, 2025",
            price = 29.99,
            category = "Sports",
            hasLottery = false,
            imageColor = 0xFFE65100,
            description = "Live broadcast of the NBA Finals on a big screen. Experience the excitement as if you were in the arena — with snacks, drinks and great atmosphere.",
            organizer = "SportEvents GmbH",
            ticketsLeft = 80
        ),
        Event(
            id = 3,
            title = "Rock am Ring",
            location = "Nürburg, Germany",
            date = "Jun 5, 2025",
            price = 149.99,
            category = "Music",
            hasLottery = true,
            imageColor = 0xFF1B5E20,
            description = "Europe's biggest rock festival. 3 days, 80 bands, unlimited energy. Headliners from around the world make history this year.",
            organizer = "Rock Events AG",
            ticketsLeft = 420
        ),
        Event(
            id = 4,
            title = "Comedy Night XXL",
            location = "Hamburg, Germany",
            date = "Jul 10, 2025",
            price = 24.99,
            category = "Comedy",
            hasLottery = false,
            imageColor = 0xFFB71C1C,
            description = "The best comedians in Germany on one stage. 3 hours of non-stop laughter — guaranteed!",
            organizer = "LaughFactory Hamburg",
            ticketsLeft = 55
        ),
        Event(
            id = 5,
            title = "Techno Festival 48h",
            location = "Frankfurt, Germany",
            date = "Sep 22, 2025",
            price = 39.99,
            category = "Music",
            hasLottery = true,
            imageColor = 0xFF0D47A1,
            description = "48 hours of non-stop techno with international DJs on 4 floors. The hardest electronic event of the year.",
            organizer = "BeatCulture GmbH",
            ticketsLeft = 310
        ),
        Event(
            id = 6,
            title = "Bundesliga Finale",
            location = "Dortmund, Germany",
            date = "May 18, 2025",
            price = 79.99,
            category = "Sports",
            hasLottery = true,
            imageColor = 0xFFF57F17,
            description = "The season finale of the Bundesliga. Who will be champion? Be there live when history is written!",
            organizer = "DFL Sports Events",
            ticketsLeft = 190
        ),
        Event(
            id = 7,
            title = "eBusiness Summit 2025",
            location = "Stuttgart, Germany",
            date = "Oct 12, 2025",
            price = 199.99,
            category = "Conference",
            hasLottery = false,
            imageColor = 0xFF00695C,
            description = "Germany's leading conference for digital business models. Keynotes, workshops and networking with top speakers from tech & business.",
            organizer = "Digital Minds GmbH",
            ticketsLeft = 40
        ),
        Event(
            id = 8,
            title = "AI & Innovation Congress",
            location = "Berlin, Germany",
            date = "Nov 3, 2025",
            price = 249.99,
            category = "Conference",
            hasLottery = true,
            imageColor = 0xFF37474F,
            description = "Two days of intensive talks on artificial intelligence, automation and the future of work. With live demos and startup pitches.",
            organizer = "TechVision Events",
            ticketsLeft = 65
        ),
        Event(
            id = 9,
            title = "Hamlet – State Theatre",
            location = "Munich, Germany",
            date = "Sep 28, 2025",
            price = 59.99,
            category = "Theater",
            hasLottery = false,
            imageColor = 0xFF4A148C,
            description = "Shakespeare's masterpiece in a modern staging. A gripping journey through power, betrayal and human depths.",
            organizer = "Bayerische Staatsoper",
            ticketsLeft = 30
        ),
        Event(
            id = 10,
            title = "Cabaret – The Musical",
            location = "Hamburg, Germany",
            date = "Oct 14, 2025",
            price = 74.99,
            category = "Theater",
            hasLottery = true,
            imageColor = 0xFF880E4F,
            description = "The legendary Broadway musical in a spectacular new production. Thrilling songs, breathtaking choreography and a moving story.",
            organizer = "Stage Entertainment",
            ticketsLeft = 120
        )
    )

    // Tickets die der User schon besitzt (Startzustand)
    val initialTickets = listOf(
        Ticket(
            id = 1,
            eventId = 1,
            eventTitle = "Summer Music Festival 2026",
            eventDate = "Wed, Jul 15, 2026",
            eventLocation = "Sunset Stadium",
            seat = "12",
            qrCode = "SP-1-SUMMER2026-ABC123",
            section = "A",
            price = 89.99,
            status = "Active"
        ),
        Ticket(
            id = 2,
            eventId = 5,
            eventTitle = "Jazz Night Live",
            eventDate = "Fri, Jun 5, 2026",
            eventLocation = "Blue Note Jazz Club",
            seat = "7",
            qrCode = "SP-5-JAZZ2026-XYZ789",
            section = "VIP",
            price = 45.00,
            status = "Active"
        ),
        Ticket(
            id = 3,
            eventId = 4,
            eventTitle = "Comedy Night XXL",
            eventDate = "Sat, Mar 10, 2025",
            eventLocation = "Hamburg, Germany",
            seat = "22",
            qrCode = "SP-4-COMEDY-DEF456",
            section = "B",
            price = 24.99,
            status = "Past"
        )
    )
}
