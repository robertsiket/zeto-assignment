import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { EdfApiService } from './edf-api.service';
import { FileInfo } from '../models/file-info.model';

describe('EdfApiService', () => {
    let service: EdfApiService;
    let httpMock: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule]
        });
        service = TestBed.inject(EdfApiService);
        httpMock = TestBed.inject(HttpTestingController);
    });

    afterEach(() => {
        httpMock.verify();
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });

    it('getEdfFiles should GET from the correct URL and return FileInfo[]', () => {
        const mockResponse: FileInfo[] = [
            {
                fileName: 'test1.edf',
                valid: true,
                identifier: 'id-123',
                recordingDate: '2024-01-01T00:00:00Z',
                patientName: 'John Doe',
                channels: [
                    {name: 'EEG Fpz-Cz', type: 'EEG'},
                    {name: 'EEG Pz-Oz', type: 'EEG'}
                ],
                recordingLengthSeconds: 3600,
                numberOfAnnotations: 5
            },
            {
                fileName: 'test2.edf',
                valid: false,
                identifier: 'id-456',
                recordingDate: '2024-02-01T00:00:00Z',
                patientName: 'Jane Doe',
                channels: [],
                recordingLengthSeconds: 0,
                numberOfAnnotations: 0
            }
        ];

        let actual: FileInfo[] | undefined;

        service.getEdfFiles().subscribe(data => {
            actual = data;
            expect(actual).toEqual(mockResponse);
        });

        const req = httpMock.expectOne('http://localhost:8080/api/edf-files');
        expect(req.request.method).toBe('GET');
        req.flush(mockResponse);
    });

    it('getEdfFiles should propagate HTTP errors', () => {
        const status = 500;
        const statusText = 'Server Error';

        service.getEdfFiles().subscribe({
            next: () => fail('expected an error, not data'),
            error: (error) => {
                expect(error.status).toBe(status);
                expect(error.statusText).toBe(statusText);
            }
        });

        const req = httpMock.expectOne('http://localhost:8080/api/edf-files');
        expect(req.request.method).toBe('GET');
        req.flush('Internal error', {status, statusText});
    });
});
