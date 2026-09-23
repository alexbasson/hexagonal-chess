require 'user_management/user_id'
require 'user_management/user'
require 'user_management/user_repository'
require 'user_management/update_user'

RSpec.describe UserManagement::UpdateUser do
  subject(:update_user) { described_class.new(user_repository) }

  let(:user_repository) { instance_double(UserManagement::UserRepository) }
  let(:user_id)         { UserManagement::UserId.new(value: 'u1') }
  let(:user)            { UserManagement::User.new(id: user_id, email: 'a@b.com', display_name: 'Alice') }

  before { allow(user_repository).to receive(:save) }

  it 'saves the updated user' do
    allow(user_repository).to receive(:find_by_id).with(user_id).and_return(user)

    update_user.call(user_id, 'new@example.com', 'Alice Updated')

    expect(user_repository).to have_received(:save) do |updated|
      expect(updated.id).to eq(user_id)
      expect(updated.email).to eq('new@example.com')
      expect(updated.display_name).to eq('Alice Updated')
    end
  end

  it 'raises when user not found' do
    allow(user_repository).to receive(:find_by_id).and_return(nil)

    expect { update_user.call(user_id, 'x@x.com', 'X') }.to raise_error(ArgumentError)
  end
end
