import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservarMesaComponent } from './reservar-mesa.component';

describe('ReservarMesaComponent', () => {
  let component: ReservarMesaComponent;
  let fixture: ComponentFixture<ReservarMesaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReservarMesaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReservarMesaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
