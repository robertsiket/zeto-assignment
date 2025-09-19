import { Component } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { FileInfo } from "../../models/edf-file-details.model";

@Component({
    selector: 'app-edf-list',
    templateUrl: './edf-list.component.html',
    styleUrls: ['./edf-list.component.scss']
})
export class EdfListComponent {
    private apiUrl = 'http://localhost:8080/api/edf-files';

    constructor(private http: HttpClient) {
    }

    getEdfFiles(): Observable<FileInfo[]> {
        return this.http.get<FileInfo[]>(this.apiUrl);
    }
}
