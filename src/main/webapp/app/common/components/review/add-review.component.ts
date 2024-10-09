import {Component, EventEmitter, inject, Input, Output} from "@angular/core";
import {CommonModule} from "@angular/common";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ReviewService} from "../../services/review.service";

@Component({
    selector: 'app-add-review',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    template: `
      <!-- add a review form -->
      <div class="w-3/5 pt-6">
        <h4 class="text-lg font-medium mb-4">Leave a Review</h4>
        <form [formGroup]="reviewForm" (ngSubmit)="handleSubmit()" class="space-y-4">
          <div class="flex items-center gap-4">
            <label for="rating" class="block text-sm font-medium text-gray-700">Your Rating</label>
            <select id="rating" formControlName="rating" class="border-gray-300 focus:ring-primary focus:border-primary block rounded-md shadow-sm sm:text-sm">
              <option [value]="5">5 Stars</option>
              <option [value]="4">4 Stars</option>
              <option [value]="3">3 Stars</option>
              <option [value]="2">2 Stars</option>
              <option [value]="1">1 Star</option>
            </select>
          </div>
          <div>
            <label for="review" class="block text-sm font-medium text-gray-700">Your Review</label>
            <textarea id="review" formControlName="comment" rows="4" class="border-gray-300 focus:ring-primary focus:border-primary mt-1 block w-full rounded-md shadow-sm sm:text-sm"></textarea>
          </div>
          <button type="submit" class="bg-primary text-white px-6 py-2 rounded-md hover:bg-primary-dark">Submit Review</button>
        </form>
      </div>
    `,
  })

export class AddReviewComponent {

  @Input() productId!: number;
  @Output() reviewSubmitted = new EventEmitter();
  reviewService = inject(ReviewService);

  reviewForm = new FormGroup({
    id: new FormControl(null),
    productId: new FormControl(this.productId, [Validators.required]),
    rating: new FormControl(5, [Validators.required]),
    comment: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
  }, { updateOn: 'submit' });


  handleSubmit() {
    window.scrollTo(0, 0);
    this.reviewForm.patchValue({productId: this.productId});
    this.reviewForm.markAllAsTouched();
    if (!this.reviewForm.valid) {
      return;
    }
    this.reviewService.createReview(this.reviewForm.value)
        .subscribe({
          next: () => {
            this.reviewForm.reset();
            this.reviewSubmitted.emit();
          },
          error: (error) => this.reviewForm.setErrors({serverError: error.error})
        });
  }

}
