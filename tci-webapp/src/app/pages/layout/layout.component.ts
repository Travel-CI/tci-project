import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './layout.component.html',
})
export class LayoutComponent implements OnInit {

  constructor() {}

  public disabled: boolean = false;
  public status: { isopen: boolean } = {isopen: false};

  public toggleDropdown($event: MouseEvent): void {
    $event.preventDefault();
    $event.stopPropagation();
    this.status.isopen = !this.status.isopen;
  }

  ngOnInit(): void {}
}
