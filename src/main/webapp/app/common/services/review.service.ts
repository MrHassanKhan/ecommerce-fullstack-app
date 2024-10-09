import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";
import {ReviewDto} from "../../model/review.model";
import {PaginatedResponse} from "../PaginatedResponse";

@Injectable({
  providedIn: 'root',
})
export class ReviewService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/reviews';

  getReviewsByProductId(productId: number): Observable<PaginatedResponse<ReviewDto>> {
    return this.http.get<PaginatedResponse<ReviewDto>>(this.resourcePath + '/getReviewsByProductId/' + productId);
  }

  getAverageRatingByProductId(productId: number) {
    return this.http.get(this.resourcePath + '/getAverageRatingByProductId/' + productId);
  }

  createReview(reviewDTO: any): Observable<ReviewDto> {
    return this.http.post(this.resourcePath, reviewDTO);
  }

}
