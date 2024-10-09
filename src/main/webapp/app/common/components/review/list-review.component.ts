import {Component, inject, Input, OnInit} from "@angular/core";
import {CommonModule} from "@angular/common";
import {ReviewService} from "../../services/review.service";
import {ReviewDto} from "../../../model/review.model";
import {ErrorHandler} from "../../error-handler.injectable";
import {AddReviewComponent} from "./add-review.component";
import {AuthenticationService} from "../../../security/authentication.service";

@Component({
  selector: 'app-list-review',
  standalone: true,
  imports: [CommonModule, AddReviewComponent],
  template: `
    <!-- review listing -->
    <div class="w-3/5 pt-6">
      <div class="text-gray-600 space-y-4">
        <!-- Single review -->
        @for(review of reviews; track review.id) {
          <div class="border-b pb-4">
            <div class="flex items-center mb-2">
              <div class="flex gap-1 text-sm text-yellow-400">
                 <span *ngFor="let star of getStars(review?.rating || 0)">
                    <i class="fa-solid fa-star" *ngIf="star === 'full'"></i>
                    <i class="fa-solid fa-star-half-stroke" *ngIf="star === 'half'"></i>
                    <i class="fa-regular fa-star" *ngIf="star === 'empty'"></i>
                </span>
              </div>
              <span class="text-xs text-gray-500 ml-3">{{review.rating}} stars ({{review.userName}})</span>
            </div>
            <p class="text-gray-800">
              {{review.comment}}
            </p>
          </div>
        }


        <!-- Add more reviews as needed -->
      </div>
    </div>

    @if(authService.isLoggedIn()){
      <app-add-review [productId]="productId || 0" (reviewSubmitted)="onReviewSubmitted()" />
    }
  `
})
export class ListReviewComponent implements OnInit {
    @Input() productId!: number;
    reviewService = inject(ReviewService);
    authService = inject(AuthenticationService);
    errorHandler = inject(ErrorHandler);

    reviews!: ReviewDto[];
    ngOnInit(): void {
        this.reviewService.getReviewsByProductId(this.productId).subscribe({
            next: (data) => {
                this.reviews = data.content;
            },
            error: (error) => this.errorHandler.handleServerError(error.error)
        })
    }

  getStars(rating: number): string[] {
    const stars = [];
    for (let i = 0; i < 5; i++) {
      if (i < rating) {
        if (i < Math.floor(rating)) {
          stars.push('full');
        } else {
          stars.push('half');
        }
      } else {
        stars.push('empty');
      }
    }
    return stars;
  }


  onReviewSubmitted() {
        this.ngOnInit();
    }

}
