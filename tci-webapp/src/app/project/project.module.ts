import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectRoutingModule } from './project.routing';
import { AddComponent } from './add/add.component'
import { DataTableModule } from 'primeng/primeng';
import {SelectModule} from 'ng2-select-compat';
import { TranslateModule } from '@ngx-translate/core';
import {FormsModule} from '@angular/forms';
import {ProjectService} from './services/project.service';
import {ProjectComponent} from './project.component';

@NgModule({
  imports: [
    FormsModule,
    CommonModule,
    ProjectRoutingModule,
    DataTableModule,
    SelectModule,
    TranslateModule.forChild()
  ],
  declarations: [
    ProjectComponent,
    AddComponent
  ],
  providers: [
    ProjectService
  ]
})
export class ProjectModule { }
