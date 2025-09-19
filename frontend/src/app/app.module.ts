import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { EdfListComponent } from './components/edf-list/edf-list.component';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { HttpClientModule } from "@angular/common/http";
import { TagModule } from "primeng/tag";
import { SkeletonModule } from "primeng/skeleton";
import { TableModule } from "primeng/table";
import { TooltipModule } from "primeng/tooltip";
import { ToolbarModule } from "primeng/toolbar";
import { AvatarModule } from "primeng/avatar";
import { NgOptimizedImage } from "@angular/common";

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
        SkeletonModule,
        TooltipModule,
        ToolbarModule,
        AvatarModule,
        NgOptimizedImage
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
