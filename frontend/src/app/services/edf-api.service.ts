import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { FileInfo } from "../models/file-info.model";

/**
 * Service to interact with the backend API for managing EDF files.
 * Provides methods to retrieve information about EDF files.
 *
 * This service is provided at the root level and can be injected into components or other services.
 */
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
