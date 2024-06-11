import { UserDTO } from "./usuario.model";

export interface Reserva {
    id: number;
    fecha: Date;
    usuario: UserDTO;
  }