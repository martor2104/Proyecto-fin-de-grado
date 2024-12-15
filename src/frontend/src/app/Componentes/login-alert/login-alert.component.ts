import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-login-alert',
  templateUrl: './login-alert.component.html',
  styleUrls: ['./login-alert.component.scss']
})
export class LoginAlertComponent {

  @Input() show: boolean = false;

  closeAlert(): void {
    this.show = false;
  }
}
