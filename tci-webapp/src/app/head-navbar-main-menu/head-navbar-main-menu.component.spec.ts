import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HeadNavbarMainMenuComponent } from './head-navbar-main-menu.component';

describe('HeadNavbarMainMenuComponent', () => {
  let component: HeadNavbarMainMenuComponent;
  let fixture: ComponentFixture<HeadNavbarMainMenuComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HeadNavbarMainMenuComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeadNavbarMainMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
