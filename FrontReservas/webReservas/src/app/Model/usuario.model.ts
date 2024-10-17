import { Role } from "./role.model";

export interface UserDTO {
    id: number | null;
    name: string;
    email: string;
    password: string;
    userRol: Role | null;
  }