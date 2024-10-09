import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import {Observable} from 'rxjs';


@Injectable({
  providedIn: 'root',
})
export class FileService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/file';

  public uploadFile(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<any>(this.resourcePath + '/upload', formData);
  }

  public serveFile(path: string): Observable<Blob> {
    return this.http.get(path, {
      responseType: 'blob'
    });
  }

}
