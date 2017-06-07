import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import {SidebarItem} from '../models/sidebar/sidebar-item.model';

@Injectable()
export class SidebarService {

  private changeSidebarSource$: Subject<SidebarItem[]> = new Subject<SidebarItem[]>();

  getSidebar() {
    return this.changeSidebarSource$;
  }

  changeSidebar(sidebar: SidebarItem[]) {
    this.changeSidebarSource$.next(sidebar);
  }
}
