import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { EdfListComponent } from './components/edf-list/edf-list.component';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { HttpClientModule } from "@angular/common/http";
import { TagModule } from "primeng/tag";
import { SkeletonModule } from "primeng/skeleton";
import { TableModule } from "primeng/table";

@NgModule({
    declarations: [
        AppComponent,
        EdfListComponent
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        BrowserAnimationsModule,
        TableModule,
        TagModule,
        SkeletonModule
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
