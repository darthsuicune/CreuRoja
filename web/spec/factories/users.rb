# Read about factories at https://github.com/thoughtbot/factory_girl

FactoryGirl.define do
	factory :user do
		sequence(:name) { |n| "Name #{n}" }
		sequence(:surname) { |n| "Surname #{n}" }
		sequence(:email) { |n| "person_#{n}@example.com" }
		password "mypassword"
		password_confirmation "mypassword"
		sequence(:resettoken) { |n| "asdfasdf #{n}" }
		resettime 0
		language "ca"
		role "volunteer"
		active true
		
		factory :admin do
			role "admin"
		end
		factory :tech do
			role "technician"
		end
	end
end
