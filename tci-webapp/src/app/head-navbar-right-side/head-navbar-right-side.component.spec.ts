import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HeadNavbarRightSideComponent } from './head-navbar-right-side.component';

describe('HeadNavbarRightSideComponent', () => {
  let component: HeadNavbarRightSideComponent;
  let fixture: ComponentFixture<HeadNavbarRightSideComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HeadNavbarRightSideComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeadNavbarRightSideComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
