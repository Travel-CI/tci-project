import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { HeadNavbarMainMenuComponent } from './head-navbar-main-menu/head-navbar-main-menu.component';
import { HeadNavbarLeftSideComponent } from './head-navbar-left-side/head-navbar-left-side.component';

@NgModule({
  declarations: [
    AppComponent,
    HeadNavbarMainMenuComponent,
    HeadNavbarLeftSideComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
