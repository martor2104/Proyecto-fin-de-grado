import { User } from "./user.model";

export interface Reservation {
    id?: number;
    reservationDate: string;
    user?: User;
  }