import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectRoutingModule } from './project.routing';
import { AddComponent } from './add/add.component'
import { DataTableModule } from 'primeng/primeng';
import {SelectModule} from "ng2-select-compat";
import { TranslateModule } from "@ngx-translate/core";

@NgModule({
  imports: [
    CommonModule,
    ProjectRoutingModule,
    DataTableModule,
    SelectModule,
    TranslateModule.forChild()
  ],
  declarations: [
    AddComponent
  ]
})
export class ProjectModule { }
