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
import {ToasterModule} from "angular2-toaster";
import { LoggerComponent } from './logger/logger.component';
import {CommandService} from "./services/command.service";
import {DialogModule} from 'primeng/primeng';
import {DropdownModule} from 'primeng/primeng';

@NgModule({
  imports: [
    FormsModule,
    CommonModule,
    ProjectRoutingModule,
    DataTableModule,
    DialogModule,
    DropdownModule,
    SelectModule,
    ToasterModule,
    TranslateModule.forChild()
  ],
  declarations: [
    ProjectComponent,
    AddComponent,
    LoggerComponent
  ],
  providers: [
    ProjectService,
    CommandService
  ]
})
export class ProjectModule { }
