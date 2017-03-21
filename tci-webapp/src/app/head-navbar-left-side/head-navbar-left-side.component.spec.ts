import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HeadNavbarLeftSideComponent } from './head-navbar-left-side.component';

describe('HeadNavbarLeftSideComponent', () => {
  let component: HeadNavbarLeftSideComponent;
  let fixture: ComponentFixture<HeadNavbarLeftSideComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HeadNavbarLeftSideComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeadNavbarLeftSideComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
