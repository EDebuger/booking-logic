// can be adjusted later...

export interface User {
    id: number;
    user_name: string;
    email: string;
    phone: string;
    user_role: 'USER' | 'ADMIN' | 'SUPERADMIN' | 'DEFUNCT';
    member_since: string; // LocalDate
    updated_at: string;   // LocalDate
}

export interface Restaurant {
    id: number;
    name: string;
    address: string;
    postal_code: string;
    service_type: 'FINE_DINING' | 'CASUAL_DINING' | 'FAST_CASUAL' | 'CONTEMPORARY_CASUAL';
    description: string;
    price_range: number;
    rating: number; // 0.00 - 5.00
    image_url: string;
    sub_of: number; // partner_company id
    company_name?: string; // Joined from partner_companies
}

export interface Booking {
    id: number;
    user_id: number;
    table_id: number;
    restaurant_name: string;
    party_size: number;
    booking_date: string; // LocalDate
    booking_time: string; // TIME HH:MM:SS | still only an option
    status: 'CONFIRMED' | 'PENDING' | 'CANCELLED';
    duration_minutes: number;
    created_at: string;
    updated_at: string;
}

export interface Notification {
    id: string;
    type: 'BOOKING_CONFIRMED' | 'BOOKING_REMINDER_DAY_BEFORE' | 'BOOKING_REMINDER_TODAY';
    booking: Booking;
    timestamp: string;
    read: boolean;
}

export interface PartnerCompany {
    id: number;
    name: string;
    telephone: string;
    email: string;
}
