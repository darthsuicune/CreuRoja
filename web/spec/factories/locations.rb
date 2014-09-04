# Read about factories at https://github.com/thoughtbot/factory_girl

FactoryGirl.define do
	factory :location do
		sequence(:id) { |n| n }
		sequence(:name) { |n| "Name #{n}" }
		sequence(:description) { |n| "Description #{n}" }
		sequence(:address) { |n| "Address #{n}" }
		sequence(:phone) { |n| "Phone #{n}" }
		sequence(:latitude) { |n| 1.5 + n }
		longitude 1.5
		location_type "MyType"
		active true
	
		factory :assembly do
			location_type "assembly"
		end
	end
end
