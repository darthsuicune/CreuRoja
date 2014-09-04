# Read about factories at https://github.com/thoughtbot/factory_girl

FactoryGirl.define do
	factory :service do
		sequence(:name) { |n| "Name #{n}" }
		sequence(:description) { |n| "Description #{n}" }
		assembly_id 1
		base_time "2014-05-11 22:16:35"
		start_time "2014-05-11 22:16:35"
		end_time "2014-05-11 23:16:35"
		code "MyCode"
	end
end
