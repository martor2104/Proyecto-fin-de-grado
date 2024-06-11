import { TableStatus } from "./tableStatus.model";

export interface Mesa {
    id: number;
    reservaId: number;
    tableStatus: TableStatus;
}