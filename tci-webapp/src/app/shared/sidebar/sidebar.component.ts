import { Component, OnInit } from '@angular/core';
import { SidebarService } from './sidebar.service';
import { SidebarItem } from '../models/sidebar/sidebar-item.model';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html'
})
export class SidebarComponent implements OnInit {

  public sidebarItems: SidebarItem[];

  constructor(private sidebarService: SidebarService) {}

  ngOnInit() {
    this.sidebarService.getSidebar().subscribe(
      items => {
        this.sidebarItems = items;
      }
    );
  }
}
