import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginAlertComponent } from './login-alert.component';

describe('LoginAlertComponent', () => {
  let component: LoginAlertComponent;
  let fixture: ComponentFixture<LoginAlertComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginAlertComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginAlertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
