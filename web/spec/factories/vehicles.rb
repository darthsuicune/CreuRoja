# Read about factories at https://github.com/thoughtbot/factory_girl

FactoryGirl.define do
	factory :vehicle do
		sequence(:brand) { |n| "Brand #{n}" }
		sequence(:model) { |n| "Model #{n}" }
		sequence(:license) { |n| "License #{n}" }
		sequence(:indicative) { |n| "Indicative #{n}" }
		vehicle_type "MyString"
		places 1
		notes "MyString"
		operative true
	end
end
