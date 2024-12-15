import { Reservation } from "./reservation.model";
import { TableStatus } from "./tableStatus.model";
import { User } from "./user.model";

export interface Mesa {
    id: number;
    reservation: Reservation | null;
    numeroMesa: number;
    ususario: User;
    tableStatus: null | TableStatus;
}