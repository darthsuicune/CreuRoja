# Read about factories at https://github.com/thoughtbot/factory_girl

FactoryGirl.define do
  factory :session do
    user_id 1
    token "MyStri" * 5
  end
end
