import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { FileInfo } from "../models/file-info.model";

@Injectable({
    providedIn: 'root'
})
export class EdfApiService {
    private apiUrl = 'http://localhost:8080/api/edf-files';

    constructor(private http: HttpClient) {
    }

    getEdfFiles(): Observable<FileInfo[]> {
        return this.http.get<FileInfo[]>(this.apiUrl);
    }
}
