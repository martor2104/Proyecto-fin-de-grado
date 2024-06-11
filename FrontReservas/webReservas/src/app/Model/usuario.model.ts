import { Role } from "./role.model";

export interface UserDTO {
    id: number;
    name: string;
    email: string;
    password: string;
    userRol: Role;
  }